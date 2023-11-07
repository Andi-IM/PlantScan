package com.github.andiim.plantscan.feature.detect.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.github.andiim.plantscan.core.domain.PostDetectionRecord
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import com.github.andiim.plantscan.core.storageUpload.StorageHelper
import com.github.andiim.plantscan.core.bitmap.asImageFromBase64
import com.github.andiim.plantscan.feature.detect.service.model.DetectionResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UploadService : Service() {
    @Inject
    lateinit var record: PostDetectionRecord

    @Inject
    lateinit var storageHelper: StorageHelper

    private var serviceJob = Job()

    private var scope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        scope.launch {
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_DETECTION, DetectionResult::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_DETECTION)
            }

            data?.let {
                val image = it.imgB64.asImageFromBase64()
                val imageLink = storageHelper.upload(image, "history/${data.userId}").first()
                record(
                    DetectionHistory(
                        null,
                        Clock.System.now(),
                        "N/A",
                        data.userId,
                        data.accuracy,
                        image = imageLink,
                    ),
                )
                Timber.d("Recorded")
            }
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        Timber.d("Finish Record")
    }

    companion object {
        const val EXTRA_DETECTION = "extra_detection"
    }
}
