package com.example.test.network

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.test.R

class TestWorkManager(appContext: Context, workerParams: WorkerParameters): CoroutineWorker(appContext, workerParams) {
    private var mediaPlayer: MediaPlayer? = null

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(){
        Log.d("TestWorkManager", "Worker Called")
        val notification = NotificationCompat.Builder(applicationContext, "audio")
            .setContentTitle("WiFi Enabled")
            .setContentText("Work Manager is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = NotificationManagerCompat.from(applicationContext)
        manager.notify(1, notification)
//        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.bg_music)
//        mediaPlayer?.setOnCompletionListener { it.release() }
//        mediaPlayer?.start()


    }


}