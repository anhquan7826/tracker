package com.anhquan.tracker_client.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.anhquan.tracker_client.ui.main.MainActivity

object NotificationUtil {
    private lateinit var CHANNEL_ID_PERSISTENCE: String

    private const val NOTIFICATION_ID_PERSISTENCE = 1

    private lateinit var persistentNotification: Notification

    fun configure(context: Context) {
        CHANNEL_ID_PERSISTENCE = "${context.packageName}.channel_persistence"
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
            ))
        }
    }

    fun getPersistentNotification(context: Context): Notification {
        if (!this::persistentNotification.isInitialized) {
            persistentNotification = buildNotification(
                context,
                CHANNEL_ID_PERSISTENCE,
                title = "Dịch vụ Tracker Client",
                content = "Tracker Admin đang hoạt động dưới nền để thu thập vị trí.",
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
}