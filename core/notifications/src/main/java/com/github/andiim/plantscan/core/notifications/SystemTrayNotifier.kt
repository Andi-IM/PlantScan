package com.github.andiim.plantscan.core.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) :
    Notifier {
    override fun postNotification() = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }
}
