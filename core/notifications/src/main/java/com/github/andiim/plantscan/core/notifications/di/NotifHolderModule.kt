package com.github.andiim.plantscan.core.notifications.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.andiim.core.notifications.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


val MAIN_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"
const val MAIN_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notification whenever work starts"
val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"

val SECOND_NOTIFICATION_CHANNEL_NAME: CharSequence = "Secondary WorkManager Notifications"

const val MAIN_CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val SECOND_CHANNEL_ID = "SECONDARY_NOTIFICATION"
@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Singleton
    @Provides
    @MainNotificationCompatBuilder
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        /*val intent = Intent(context, MyReceiver::class.java).apply {
            putExtra("MESSAGE", "Clicked!")
        }

        val flag = PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, flag
        )*/

        return NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(MAIN_NOTIFICATION_CHANNEL_DESCRIPTION)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPublicVersion(
                NotificationCompat.Builder(context, MAIN_CHANNEL_ID)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(MAIN_NOTIFICATION_CHANNEL_DESCRIPTION)
                    .build()
            )
        // addAction(0, "ACTION", pendingIntent)
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