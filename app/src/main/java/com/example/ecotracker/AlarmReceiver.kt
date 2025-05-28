package com.example.ecotracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("AlarmReceiver", "Before triggering notification")
        triggerNotification(context)
    }


    private fun triggerNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "tips_channel_id"
        val channel = NotificationChannel(channelId, "Weekly Tips", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("New Weekly Tip")
            .setContentText("Check out the new tip for this week!")
            .setSmallIcon(R.drawable.ic_outline_notifications_active_24)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}