package com.example.test.network

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.test.R

class AudioService: Service() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> {
                start()
            }
            Actions.STOP.toString()-> {
                mediaPlayer.stop()
                mediaPlayer.release()
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions{
        START,
        STOP

    }

    private fun start(){
        val stopIntent = Intent(this, AudioService::class.java)
        stopIntent.action= Actions.STOP.toString()
        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this,"audio")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Audio Service")
            .setContentText("Audio Service is running")
            .addAction(R.drawable.stop, "Stop", pendingStopIntent)
            .build()


        startForeground(1,notification)
    }

}

