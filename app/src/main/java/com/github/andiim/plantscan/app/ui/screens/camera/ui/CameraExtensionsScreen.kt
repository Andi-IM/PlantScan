/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.andiim.plantscan.app.ui.screens.camera.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.TypedValue
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.andiim.plantscan.app.databinding.FragmentCameraBinding
import com.github.andiim.plantscan.app.ui.screens.camera.composeUtils.NonPermissionScreen
import com.github.andiim.plantscan.app.ui.screens.camera.composeUtils.PhotoPreviewScreen
import com.github.andiim.plantscan.app.ui.screens.camera.adapter.CameraExtensionsSelectorAdapter
import com.github.andiim.plantscan.app.ui.screens.camera.model.CameraUiAction
import com.github.andiim.plantscan.app.ui.screens.camera.util.ToastUtil
import com.github.andiim.plantscan.app.ui.screens.camera.viewstate.CameraPreviewScreenViewState
import com.github.andiim.plantscan.app.ui.screens.camera.viewstate.CaptureScreenViewState
import com.github.andiim.plantscan.app.ui.screens.camera.viewstate.PostCaptureScreenViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Displays the camera preview and captured photo. Encapsulates the details of how the screen is
 * constructed and exposes a set of explicit operations clients can perform on the screen.
 */
@SuppressLint("ClickableViewAccessibility")
class CameraExtensionsScreen(
    private val context: Context,
    private val binding: FragmentCameraBinding
) {

  private companion object {
    // animation constants for focus point
    private const val SPRING_STIFFNESS_ALPHA_OUT = 100f
    private const val SPRING_STIFFNESS = 800f
    private const val SPRING_DAMPING_RATIO = 0.35f
  }

  private val cameraShutterButton: View = binding.cameraShutter
  private val closeCameraButton: View = binding.closeCamera
  private val switchLensButton = binding.switchLens
  private val addFromGalleryButton = binding.addFromGallery
  private val extensionSelector: RecyclerView = binding.extensionSelector
  private val extensionsAdapter: CameraExtensionsSelectorAdapter
  private val focusPointView: View = binding.focusPoint

  val previewView: PreviewView = binding.previewView

  private val _action: MutableSharedFlow<CameraUiAction> = MutableSharedFlow()
  val action: Flow<CameraUiAction> = _action

  private val rationaleState = mutableStateOf(false)
  private val photoState = mutableStateOf("")

  init {
    val snapHelper = CenterItemSnapHelper()

    extensionsAdapter = CameraExtensionsSelectorAdapter { view -> onItemClick(view) }
    extensionSelector.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
      adapter = extensionsAdapter
      addItemDecoration(OffsetCenterItemDecoration())
      addOnScrollListener(
          object : RecyclerView.OnScrollListener() {
            private var snapPosition = RecyclerView.NO_POSITION

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
              if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager ?: return
                val snapView = snapHelper.findSnapView(layoutManager) ?: return
                val newSnapPosition = layoutManager.getPosition(snapView)
                onItemSelected(snapPosition, newSnapPosition)
                snapPosition = newSnapPosition
              }
            }

            private fun onItemSelected(oldPosition: Int, newPosition: Int) {
              if (oldPosition == newPosition) return
              selectItem(newPosition)
              extensionsAdapter.currentList[newPosition]?.let {
                binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                  _action.emit(CameraUiAction.SelectCameraExtension(it.extensionMode))
                }
              }
            }

            private fun selectItem(position: Int) {
              val data =
                  extensionsAdapter.currentList.mapIndexed { index, cameraExtensionModel ->
                    cameraExtensionModel.copy(selected = position == index)
                  }
              extensionsAdapter.submitList(data)
            }
          })
    }

    snapHelper.attachToRecyclerView(extensionSelector)

    cameraShutterButton.setOnClickListener {
      binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        _action.emit(CameraUiAction.ShutterButtonClick)
      }
    }

    switchLensButton.setOnClickListener { switchLens(binding.root, it) }

    addFromGalleryButton.setOnClickListener {
      binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        _action.emit(CameraUiAction.AddFromGalleryClick)
      }
    }

    binding.photoPreview.apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
      setContent {
        val painter =
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).data(photoState.value).build())

        if (painter.state is AsyncImagePainter.State.Error) {
          val error =
              (painter.state as AsyncImagePainter.State.Error).result.throwable.localizedMessage
          ToastUtil.showToast(LocalContext.current, "Error: $error")
        }

        PhotoPreviewScreen(
            imageUri = painter,
            imageDescription = null,
            onDetect = {
              binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                _action.emit(CameraUiAction.ActionDetect(photoState.value))
              }
            },
            onDispose = {
              binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                _action.emit(CameraUiAction.ClosePhotoPreviewClick)
              }
            })
      }
    }

    closeCameraButton.setOnClickListener {
      binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        _action.emit(CameraUiAction.CloseCameraClick)
      }
    }

    binding.permissionsRationaleContainer.apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
      setContent {
        NonPermissionScreen(shouldShowRationale = rationaleState.value) {
          binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            _action.emit(CameraUiAction.RequestPermissionClick)
          }
        }
      }
    }

    val gestureDetector =
        GestureDetectorCompat(
            context,
            object : SimpleOnGestureListener() {
              override fun onDown(e: MotionEvent): Boolean = true

              override fun onSingleTapUp(e: MotionEvent): Boolean {
                val meteringPointFactory = previewView.meteringPointFactory
                val focusPoint = meteringPointFactory.createPoint(e.x, e.y)
                binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                  _action.emit(CameraUiAction.Focus(focusPoint))
                }
                showFocusPoint(focusPointView, e.x, e.y)
                return true
              }

              override fun onDoubleTap(e: MotionEvent): Boolean {
                switchLens(binding.root, switchLensButton)
                return true
              }
            })

    val scaleGestureDetector =
        ScaleGestureDetector(
            context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
              override fun onScale(detector: ScaleGestureDetector): Boolean {
                binding.root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                  _action.emit(CameraUiAction.Scale(detector.scaleFactor))
                }
                return true
              }
            })

    previewView.setOnTouchListener { _, event ->
      var didConsume = scaleGestureDetector.onTouchEvent(event)
      if (!scaleGestureDetector.isInProgress) {
        didConsume = gestureDetector.onTouchEvent(event)
      }
      didConsume
    }
  }

  fun setCaptureScreenViewState(state: CaptureScreenViewState) {
    setCameraScreenViewState(state.cameraPreviewScreenViewState)
    when (state.postCaptureScreenViewState) {
      PostCaptureScreenViewState.PostCaptureScreenHiddenViewState -> hidePhoto()
      is PostCaptureScreenViewState.PostCaptureScreenVisibleViewState ->
          showPhoto(state.postCaptureScreenViewState.uri)
    }
  }

  fun showCaptureError(errorMessage: String) {
    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
  }

  fun hidePermissionsRequest() {
    binding.permissionsRationaleContainer.isVisible = false
  }

  fun showPermissionsRequest(shouldShowRationale: Boolean) {
    closeCameraButton.isVisible = true
    binding.permissionsRationaleContainer.isVisible = true
    rationaleState.value = shouldShowRationale
  }

  private fun showPhoto(uri: Uri?) {
    if (uri == null) return
    binding.photoPreview.isVisible = true
    photoState.value = uri.toString()
  }

  private fun hidePhoto() {
    binding.photoPreview.isVisible = false
  }

  private fun setCameraScreenViewState(state: CameraPreviewScreenViewState) {
    closeCameraButton.isEnabled = state.backButtonViewState.isEnabled
    closeCameraButton.isVisible = state.backButtonViewState.isVisible

    cameraShutterButton.isEnabled = state.shutterButtonViewState.isEnabled
    cameraShutterButton.isVisible = state.shutterButtonViewState.isVisible

    switchLensButton.isEnabled = state.switchLensButtonViewState.isEnabled
    switchLensButton.isVisible = state.switchLensButtonViewState.isVisible

    extensionSelector.isVisible = state.extensionsSelectorViewState.isVisible
    extensionsAdapter.submitList(state.extensionsSelectorViewState.extensions)
  }

  private fun onItemClick(view: View) {
    val layoutManager = extensionSelector.layoutManager as? LinearLayoutManager ?: return
    val viewMiddle = view.left + view.width / 2
    val middle = layoutManager.width / 2
    val dx = viewMiddle - middle
    extensionSelector.smoothScrollBy(dx, 0)
  }

  private fun switchLens(root: View, switchLensButton: View) {
    root.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
      _action.emit(CameraUiAction.SwitchCameraClick)
    }

    switchLensButton.animate().apply {
      rotation(180f)
      duration = 300L
      setListener(
          object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
              switchLensButton.rotation = 0f
            }
          })
      start()
    }
  }

  private fun showFocusPoint(view: View, x: Float, y: Float) {
    val drawable = FocusPointDrawable()
    val strokeWidth =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, context.resources.displayMetrics)
    drawable.setStrokeWidth(strokeWidth)

    val alphaAnimation =
        SpringAnimation(view, DynamicAnimation.ALPHA, 1f).apply {
          spring.stiffness = SPRING_STIFFNESS
          spring.dampingRatio = SPRING_DAMPING_RATIO

          addEndListener { _, _, _, _ ->
            SpringAnimation(view, DynamicAnimation.ALPHA, 0f)
                .apply {
                  spring.stiffness = SPRING_STIFFNESS_ALPHA_OUT
                  spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                }
                .start()
          }
        }
    val scaleAnimationX =
        SpringAnimation(view, DynamicAnimation.SCALE_X, 1f).apply {
          spring.stiffness = SPRING_STIFFNESS
          spring.dampingRatio = SPRING_DAMPING_RATIO
        }
    val scaleAnimationY =
        SpringAnimation(view, DynamicAnimation.SCALE_Y, 1f).apply {
          spring.stiffness = SPRING_STIFFNESS
          spring.dampingRatio = SPRING_DAMPING_RATIO
        }

    view.apply {
      background = drawable
      isVisible = true
      translationX = x - width / 2f
      translationY = y - height / 2f
      alpha = 0f
      scaleX = 1.5f
      scaleY = 1.5f
    }

    alphaAnimation.start()
    scaleAnimationX.start()
    scaleAnimationY.start()
  }
}
