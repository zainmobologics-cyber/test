package com.example.test.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.test.network.AudioService

@Composable
fun AudioPlayerScreen(audioUri: String) {
    val context = LocalContext.current
    val player= remember {
        ExoPlayer.Builder(context).build()
    }
    var isPlaying by remember {
        mutableStateOf(player.isPlaying)
    }
    var isMute by remember {mutableStateOf(false)}
    var volume by remember {
        mutableFloatStateOf(player.volume)
    }


    DisposableEffect(player) {
        val mediaItem= MediaItem.fromUri(audioUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        onDispose {
            player.release()
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ,modifier = Modifier.fillMaxSize()
    ) {
        Card(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFDA088)
            )) {


            Row(horizontalArrangement = Arrangement.Center,
                 modifier = Modifier
                     .padding(10.dp)
                     .fillMaxWidth()) {
                IconButton(
                    onClick = {
                        player.seekTo(player.currentPosition + 10000)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Previous"
                    )
                }
                IconButton(
                    onClick = {

                        if (player.isPlaying) {
                            player.pause()
                            isPlaying=false
                            Intent(context, AudioService::class.java).also {
                                it.action= AudioService.Actions.STOP.toString()
                                context.startService(it)
                            }
                        } else {
                            Intent(context, AudioService::class.java).also {
                                it.action= AudioService.Actions.START.toString()
                                context.startService(it)
                            }
                            player.play()
                            isPlaying=true
                        }
                    }
                ) {
                    if (isPlaying){
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause"
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play"
                        )
                    }
                }
                IconButton(
                    onClick = {
                        player.seekTo(player.currentPosition - 10000)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Previous"
                    )
                }
                IconButton(
                    onClick = {
                        if (player.volume==0f){
                            player.volume=1f
                            isMute=false
                        }else{
                            player.volume=0f
                            isMute=true
                        }
                    }
                ) {
                    if (isMute){
                        Icon(
                            imageVector = Icons.Default.VolumeOff,
                            contentDescription = "Mute"
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Unmute"
                        )
                    }
                }

            }
            Slider(
                value = volume,
                onValueChange = {
                    volume = it
                    player.volume = it
                },
                valueRange = 0f..1f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

        }
    }
//    AndroidView(
//        factory = {ctx->
//            PlayerView(ctx).apply {
//                this.player=player
//                useController=true
//
//            }
//
//        },
//        modifier = Modifier.fillMaxSize()
//    )

}

