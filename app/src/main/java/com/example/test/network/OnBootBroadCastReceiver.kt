package com.example.test.network

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.test.R

class OnBootBroadCastReceiver: BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("message")
        if (message!=null){
            val notification= NotificationCompat.Builder(context!!,"audio")
                .setContentTitle("Alarm Triggered")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

            val notificationManager= NotificationManagerCompat.from(context)
            notificationManager.notify(1,notification)

        }

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Intent(context, AudioService::class.java).also {
                it.action= AudioService.Actions.START.toString()
                context?.startService(it)
            }

        }
    }
}