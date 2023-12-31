package com.anhquan.tracker_admin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews

object NotificationUtil {
    private lateinit var CHANNEL_ID_PERSISTENCE: String
    private lateinit var CHANNEL_ID_UPDATE_NOTIFICATION: String

    private lateinit var persistentNotification: Notification

    fun configure(context: Context) {
        CHANNEL_ID_PERSISTENCE = "${context.packageName}.channel_persistence"
        CHANNEL_ID_UPDATE_NOTIFICATION = "\"${context.packageName}.channel_location_update"
        context.getSystemService(NotificationManager::class.java).apply {
            createNotificationChannels(listOf(
                NotificationChannel(
                    CHANNEL_ID_PERSISTENCE,
                    "Persistent Notification",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "This channel is used for Tracker background service."
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                },
                NotificationChannel(
                    CHANNEL_ID_UPDATE_NOTIFICATION,
                    "Location Update Notification",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "This channel is used for Tracker location update notification."
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                },
            ))
        }
    }

    fun getPersistentNotification(context: Context): Notification {
        if (!this::persistentNotification.isInitialized) {
            persistentNotification = buildNotification(
                context,
                CHANNEL_ID_PERSISTENCE,
                ongoing = true,
                requestCode = 0,
                intentExtras = Bundle().apply {
                    putBoolean("refresh", true)
                }
            )
        }
        return persistentNotification
    }

    private fun buildNotification(
        context: Context,
        channel: String,
        title: String? = null,
        content: String? = null,
        view: RemoteViews? = null,
        expandedView: RemoteViews? = null,
        ongoing: Boolean = false,
        requestCode: Int,
        intentExtras: Bundle? = null
    ): Notification {
        return Notification.Builder(context, channel).apply {
            if (title != null) setContentTitle(title)
            if (content != null) setContentText(content)
            if (view != null) setCustomContentView(view)
            if (expandedView != null) setCustomBigContentView(expandedView)
            setSmallIcon(R.drawable.baseline_location_on_24)
            setOngoing(ongoing)
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    requestCode,
                    Intent(context, MainActivity::class.java).apply {
                        if (intentExtras != null) putExtras(intentExtras)
                    },
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        }.build()
    }

    fun showDeviceLocationNotification(context: Context, deviceName: String, lat: Double, lon: Double) {
        val warningNotification = buildNotification(
            context,
            CHANNEL_ID_UPDATE_NOTIFICATION,
            title = "$deviceName thay đổi vị trí!",
            content = "$lat, $lon",
            requestCode = 1,
        )
        context.getSystemService(NotificationManager::class.java).notify(
            2,
            warningNotification
        )
    }
}