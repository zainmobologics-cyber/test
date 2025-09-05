package com.example.test.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.test.R
import com.example.test.network.AudioService
import java.io.File
import java.util.concurrent.TimeUnit

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
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.musicplaying))


    DisposableEffect(player) {
        val mediaItem= MediaItem.fromUri(audioUri)
        player.setMediaItem(mediaItem)
        player.prepare()
        onDispose {
            player.release()
        }
    }
    val audioTitle: String? = getAudioFileName(context,audioUri.toUri())


    val progress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = if (isPlaying) infiniteRepeatable(animation = tween(durationMillis = 1500), repeatMode = RepeatMode.Restart) else tween(1000),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ,modifier = Modifier.fillMaxSize()
    ) {
        Card(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFDA088)
            )) {
            LottieAnimation(composition = composition,
                progress=progress,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(280.dp)
            )
            Text(text = audioTitle?:"Unknown",
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(15.dp))

            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()) {
                IconButton(
                    onClick = {
                        player.seekTo(player.currentPosition + 10000)
                    }
                ) {
                    Icon( modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Previous"
                    )
                }
                IconButton(
                    onClick = {
                        if (player.isPlaying) {
                            player.pause()
                            isPlaying=false
//                            Intent(context, AudioService::class.java).also {
//                                it.action= AudioService.Actions.STOP.toString()
//                                context.startService(it)
//                            }
                        } else {
                            isPlaying=true
//                            Intent(context, AudioService::class.java).also {
//                                it.action= AudioService.Actions.START.toString()
//                                it.putExtra("AUDIO_URI", audioUri)
//                                context.startService(it)
//                            }
                            player.play()
                        }
                    }
                ) {
                    if (isPlaying){
                        Icon(  modifier = Modifier.size(50.dp),

                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pause"
                        )
                    }else{
                        Icon( modifier = Modifier.size(50.dp),
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
                    Icon( modifier = Modifier.size(32.dp),
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
                        Icon( modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.VolumeOff,
                            contentDescription = "Mute"
                        )
                    }else{
                        Icon( modifier = Modifier.size(35.dp),
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

fun getAudioFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    if (uri.scheme == "content") {
        val projection = arrayOf(MediaStore.Audio.Media.DISPLAY_NAME)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                fileName = cursor.getString(nameIndex)
            }
        }
    } else if (uri.scheme == "file") {
        // If the URI is a file URI, just get the name from the path
        fileName = File(uri.path!!).name
    }
    return fileName
}



