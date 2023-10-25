package com.github.andiim.plantscan.app.utils.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.app.NotificationManagerCompat
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.receiver.MyReceiver
import com.github.andiim.plantscan.app.utils.MAIN_CHANNEL_ID
import com.github.andiim.plantscan.app.utils.MAIN_NOTIFICATION_CHANNEL_DESCRIPTION
import com.github.andiim.plantscan.app.utils.MAIN_NOTIFICATION_CHANNEL_NAME
import com.github.andiim.plantscan.app.utils.NOTIFICATION_TITLE
import com.github.andiim.plantscan.app.utils.SECOND_CHANNEL_ID
import com.github.andiim.plantscan.app.utils.SECOND_NOTIFICATION_CHANNEL_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    @MainNotificationCompatBuilder
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("MESSAGE", "Clicked!")
        }

        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, flag
        )

        return NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(MAIN_NOTIFICATION_CHANNEL_DESCRIPTION)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_PRIVATE)
            .setPublicVersion(
                NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(MAIN_NOTIFICATION_CHANNEL_DESCRIPTION)
                    .build()
            )
            .addAction(0, "ACTION", pendingIntent)
        // setContentIntent(clickPendingIntent)
    }

    @Singleton
    @Provides
    @SecondNotificationCompatBuilder
    fun provideNotificationCompatBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, SECOND_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        val channel1 = NotificationChannel(
            MAIN_CHANNEL_ID,
            MAIN_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val channel2 = NotificationChannel(
            SECOND_CHANNEL_ID,
            SECOND_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel1)
        notificationManager.createNotificationChannel(channel2)
        return notificationManager
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainNotificationCompatBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecondNotificationCompatBuilder