package com.github.andiim.plantscan.app.ui.screens.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.extensions.ExtensionMode
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.databinding.FragmentCameraBinding
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.ui.screens.camera.adapter.CameraExtensionItem
import com.github.andiim.plantscan.app.ui.screens.camera.model.CameraState
import com.github.andiim.plantscan.app.ui.screens.camera.model.CameraUiAction
import com.github.andiim.plantscan.app.ui.screens.camera.model.CaptureState
import com.github.andiim.plantscan.app.ui.screens.camera.model.PermissionState
import com.github.andiim.plantscan.app.ui.screens.camera.ui.CameraExtensionsScreen
import com.github.andiim.plantscan.app.ui.screens.camera.ui.doOnLaidOut
import com.github.andiim.plantscan.app.ui.screens.camera.viewstate.CaptureScreenViewState
import com.github.andiim.plantscan.app.ui.screens.camera.viewstate.PostCaptureScreenViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraFragment : Fragment() {
  var onBackPressed: (() -> Unit)? = null
  var toDetect: ((String, Boolean) -> Unit)? = null

  private val extensionName =
      mapOf(
          ExtensionMode.AUTO to R.string.camera_mode_auto,
          ExtensionMode.NIGHT to R.string.camera_mode_night,
          ExtensionMode.HDR to R.string.camera_mode_hdr,
          ExtensionMode.FACE_RETOUCH to R.string.camera_mode_face_retouch,
          ExtensionMode.BOKEH to R.string.camera_mode_bokeh,
          ExtensionMode.NONE to R.string.camera_mode_none,
      )
  // tracks the current view state
  private val captureScreenViewState = MutableStateFlow(CaptureScreenViewState())

  // handles back press if the current screen is the photo post capture screen
  private val postCaptureBackPressedCallback =
      object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
          lifecycleScope.launch { closePhotoPreview() }
        }
      }

  private lateinit var extensionsScreen: CameraExtensionsScreen

  // view model for operating on the camera and capturing a photo
  private val viewModel: CameraViewModel by viewModels()

  // monitors changes in camera permission state
  private lateinit var permissionState: MutableStateFlow<PermissionState>

  private var _binding: FragmentCameraBinding? = null
  private val binding
    get() = _binding!!
  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    _binding = FragmentCameraBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    extensionsScreen = CameraExtensionsScreen(binding.root.context as Context, binding)

    // consume and dispatch the current view state to update the camera extensions screen
    lifecycleScope.launch {
      captureScreenViewState.collectLatest { state ->
        extensionsScreen.setCaptureScreenViewState(state)
        postCaptureBackPressedCallback.isEnabled =
            state.postCaptureScreenViewState is
                PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
      }
    }

    activity?.apply {
      onBackPressedDispatcher.addCallback(viewLifecycleOwner, postCaptureBackPressedCallback)
    }

    // initialize the permission state flow with the current camera permission status
    permissionState = MutableStateFlow(getCurrentPermissionState())

    val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
          if (isGranted) {
            lifecycleScope.launch { permissionState.emit(PermissionState.Granted) }
          } else {
            lifecycleScope.launch { permissionState.emit(PermissionState.Denied(true)) }
          }
        }

    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
          lifecycleScope.launch { viewModel.openGallery(uri) }
        }

    lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.RESUMED) {
        // check the current permission state every time upon the activity is resumed
        permissionState.emit(getCurrentPermissionState())
      }
    }

    // Consumes actions emitted by the UI and performs the appropriate operation associated with
    // the view model or permission flow.
    // Note that this flow is a shared flow and will not emit the last action unlike the state
    // flows exposed by the view model for consuming UI state.
    lifecycleScope.launch {
      extensionsScreen.action.collectLatest { action ->
        when (action) {
          is CameraUiAction.SelectCameraExtension -> {
            viewModel.setExtensionMode(action.extension)
          }
          CameraUiAction.AddFromGalleryClick -> {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
          }
          CameraUiAction.ShutterButtonClick -> {
            viewModel.capturePhoto()
          }
          CameraUiAction.SwitchCameraClick -> {
            viewModel.switchCamera()
          }
          CameraUiAction.ClosePhotoPreviewClick -> {
            closePhotoPreview()
          }
          is CameraUiAction.ActionDetect -> {
            val uri = action.uri
            toDetect?.invoke(Direction.Detect.createRoute(uri), false)
          }
          CameraUiAction.CloseCameraClick -> {
            closeCamera()
          }
          CameraUiAction.RequestPermissionClick -> {
            requestPermissionsLauncher.launch(Manifest.permission.CAMERA)
          }
          is CameraUiAction.Focus -> {
            viewModel.focus(action.meteringPoint)
          }
          is CameraUiAction.Scale -> {
            viewModel.scale(action.scaleFactor)
          }
        }
      }
    }

    // Consume state emitted by the view model to render the Photo Capture state.
    // Upon collecting this state, the last emitted state will be immediately received.
    lifecycleScope.launch {
      viewModel.captureUiState.collectLatest { state ->
        when (state) {
          CaptureState.CaptureNotReady -> {
            captureScreenViewState.emit(
                captureScreenViewState.value
                    .updatePostCaptureScreen {
                      PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
                    }
                    .updateCameraScreen {
                      it.enableBackButton(true)
                          .enableCameraShutter(true)
                          .enableSwitchLens(true)
                          .enableGalleryButton(true)
                    })
          }
          CaptureState.CaptureReady -> {
            captureScreenViewState.emit(
                captureScreenViewState.value.updateCameraScreen {
                  it.enableBackButton(true)
                      .enableCameraShutter(true)
                      .enableSwitchLens(true)
                      .enableGalleryButton(true)
                })
          }
          CaptureState.OpenGallery -> {
            captureScreenViewState.emit(
                captureScreenViewState.value.updateCameraScreen {
                  it.enableBackButton(false)
                      .enableCameraShutter(false)
                      .enableSwitchLens(false)
                      .enableGalleryButton(false)
                })
          }
          is CaptureState.ImageObtained -> {
            lifecycleScope.launch {
              viewModel.stopPreview()
              captureScreenViewState.emit(
                  captureScreenViewState.value
                      .updatePostCaptureScreen {
                        val uri = state.uri
                        if (uri != null) {
                          PostCaptureScreenViewState.PostCaptureScreenVisibleViewState(uri)
                        } else {
                          PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
                        }
                      }
                      .updateCameraScreen { it.hideCameraControls() })
            }
          }
          CaptureState.CaptureStarted -> {
            captureScreenViewState.emit(
                captureScreenViewState.value.updateCameraScreen {
                  it.enableBackButton(false)
                      .enableCameraShutter(false)
                      .enableSwitchLens(false)
                      .enableGalleryButton(false)
                })
          }
          is CaptureState.CaptureFinished -> {
            viewModel.stopPreview()
            captureScreenViewState.emit(
                captureScreenViewState.value
                    .updatePostCaptureScreen {
                      val uri = state.outputResults.savedUri
                      if (uri != null) {
                        PostCaptureScreenViewState.PostCaptureScreenVisibleViewState(uri)
                      } else {
                        PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
                      }
                    }
                    .updateCameraScreen { it.hideCameraControls() })
          }
          is CaptureState.CaptureFailed -> {
            extensionsScreen.showCaptureError("Couldn't take photo")
            viewModel.startPreview(viewLifecycleOwner, extensionsScreen.previewView)
            captureScreenViewState.emit(
                captureScreenViewState.value.updateCameraScreen {
                  it.showCameraControls().enableCameraShutter(true).enableSwitchLens(true)
                })
          }
        }
      }
    }

    // Because camera state is dependent on the camera permission status, we combine both camera
    // UI state and permission state such that each emission accurately captures the current
    // permission status and camera UI state.
    // The camera permission is always checked to see if it's granted. If it isn't then stop
    // interacting with the camera and display the permission request screen. The user can tap
    // on "Turn On" to request permissions.
    lifecycleScope.launch {
      permissionState
          .combine(viewModel.cameraUiState) { permissionState, cameraUiState ->
            Pair(permissionState, cameraUiState)
          }
          .collectLatest { (permissionState, cameraUiState) ->
            when (permissionState) {
              PermissionState.Granted -> {
                extensionsScreen.hidePermissionsRequest()
              }
              is PermissionState.Denied -> {
                if (cameraUiState.cameraState != CameraState.PREVIEW_STOPPED) {
                  extensionsScreen.showPermissionsRequest(permissionState.shouldShowRationale)
                  return@collectLatest
                }
              }
            }

            when (cameraUiState.cameraState) {
              CameraState.NOT_READY -> {
                captureScreenViewState.emit(
                    captureScreenViewState.value
                        .updatePostCaptureScreen {
                          PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
                        }
                        .updateCameraScreen {
                          it.showCameraControls().enableCameraShutter(false).enableSwitchLens(false)
                        })
                viewModel.initializeCamera()
              }
              CameraState.READY -> {
                extensionsScreen.previewView.doOnLaidOut {
                  viewModel.startPreview(viewLifecycleOwner, extensionsScreen.previewView)
                }
                captureScreenViewState.emit(
                    captureScreenViewState.value.updateCameraScreen { s ->
                      s.showCameraControls()
                          .setAvailableExtensions(
                              cameraUiState.availableExtensions.map {
                                CameraExtensionItem(
                                    it,
                                    getString(extensionName[it]!!),
                                    cameraUiState.extensionMode == it)
                              })
                    })
              }
              CameraState.PREVIEW_STOPPED -> Unit
            }
          }
    }
  }

  private fun closeCamera() {
    onBackPressed?.invoke()
  }

  private suspend fun closePhotoPreview() {
    captureScreenViewState.emit(
        captureScreenViewState.value
            .updateCameraScreen { state -> state.showCameraControls() }
            .updatePostCaptureScreen {
              PostCaptureScreenViewState.PostCaptureScreenHiddenViewState
            })
    viewModel.startPreview(viewLifecycleOwner, extensionsScreen.previewView)
  }
  private fun getCurrentPermissionState(): PermissionState {
    val status = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
    return if (status == PackageManager.PERMISSION_GRANTED) {
      PermissionState.Granted
    } else {
      PermissionState.Denied(
          ActivityCompat.shouldShowRequestPermissionRationale(
              requireActivity(), Manifest.permission.CAMERA))
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
