package com.example.test.network

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresPermission
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.test.R

class AudioService: Service() {
//    private var mediaPlayer: MediaPlayer? = null
    private var player:ExoPlayer?=null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        super.onCreate()

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> {
                start(intent)
            }
            Actions.STOP.toString()-> {
                stopSelf()
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions{
        START,
        STOP,

    }


    private fun start(intent: Intent){
//        mediaPlayer = MediaPlayer.create(this, R.raw.bg_music)
//        mediaPlayer?.start()

        val uri=intent.getStringExtra("AUDIO_URI")
        Log.d("Audio URI", "$uri")
        if (player == null) {
            player = ExoPlayer.Builder(this).build()
        }

        uri?.let {
            val mediaItem = MediaItem.fromUri(it)
            player?.setMediaItem(mediaItem)
            player?.prepare()
            player?.play()
        }
        val stopIntent = Intent(this, AudioService::class.java)
        stopIntent.action= Actions.STOP.toString()
        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this,"audio")
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setSmallIcon(R.drawable.music_note_symbol)
            .setContentTitle("Audio Service")
            .setContentText("Audio Service is running")
            .addAction(R.drawable.outline_stop_24, "Stop",pendingStopIntent  )
             .setColor(ContextCompat.getColor(this, R.color.purple_200))
            .build()


        startForeground(1,notification)
    }

    override fun onDestroy() {
        player?.stop()
        player?.release()
        super.onDestroy()

    }

}

