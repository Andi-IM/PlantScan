package com.github.andiim.plantscan.app.worker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.andiim.plantscan.app.utils.ACTION_UPLOAD_STATUS
import com.github.andiim.plantscan.app.utils.di.SecondNotificationCompatBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.storage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import kotlin.math.roundToInt

private const val NOTIFICATION_ID = 1

@HiltWorker
class FileUploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher,
    @SecondNotificationCompatBuilder
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
) : CoroutineWorker(context, params) {
    private val storageRef = Firebase.storage.getReference("testing")
    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        val uri = inputData.getString("fileUri")!!.toUri()
        return@withContext try {
            uploadImageFromUri(uri)
        } catch (e: Exception) {
            Result.failure()
        } catch (e: IOException) {
            Result.failure()
        }
    }

    private fun uploadImageFromUri(fileUri: Uri): Result {
        fileUri.lastPathSegment?.let {
            val photoRef = storageRef.child(it)
            Timber.tag("UploadWorker").d(it)
            photoRef.putFile(fileUri)
                .addOnProgressListener { (bytesTransferred, totalByteCount) ->
                    val progress = (100.0 * bytesTransferred) / totalByteCount
                    if (checkPermission()) {
                        notificationManager.notify(
                            3,
                            notificationBuilder
                                .setContentTitle("Uploading")
                                .setContentText("$progress%")
                                .setProgress(100, progress.roundToInt(), false)
                                .build()
                        )
                    }

                }
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    Timber.tag("UploadWorker").d("uploadFromUri: upload success!")
                    photoRef.downloadUrl
                }
                .addOnSuccessListener {
                    val notifyFinishEvent = Intent().setAction(ACTION_UPLOAD_STATUS)
                    applicationContext.sendBroadcast(notifyFinishEvent)
                }
                .addOnFailureListener { exception ->
                    Timber.tag("UploadWorker").w(exception, "uploadImageFromUri:onFailure ")
                }
        }
        return Result.success()
    }


    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }
}
