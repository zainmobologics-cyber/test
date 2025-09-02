package com.example.test.network

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.common.util.UnstableApi
import com.example.test.R

class AudioService: Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        super.onCreate()

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> {
                Log.d("AudioService", "AudioService started")
                start(mediaPlayer)
            }
            Actions.STOP.toString()-> {
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    enum class Actions{
        START,
        STOP

    }

    private fun start(mediaItem: MediaPlayer?){
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_music)
        mediaPlayer?.start()

//        if (player == null) {
//            player = ExoPlayer.Builder(this).build()
//        }
//        uri?.let {
//            val mediaItem = MediaItem.fromUri(it)
//            player?.setMediaItem(mediaItem)
//            player?.prepare()
//            player?.play()
//        }
        val stopIntent = Intent(this, AudioService::class.java)
        stopIntent.action= Actions.STOP.toString()
        val pendingStopIntent = PendingIntent.getService(this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this,"audio")
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setSmallIcon(R.drawable.music_note_symbol)
            .setContentTitle("Audio Service")
            .setContentText("Audio Service is running")
            .addAction(R.drawable.stop, "Stop",pendingStopIntent  )
             .setColor(ContextCompat.getColor(this, R.color.purple_200))
            .build()


        startForeground(1,notification)
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        super.onDestroy()

    }

}

