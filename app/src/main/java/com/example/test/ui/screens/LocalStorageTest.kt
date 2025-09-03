package com.example.test.ui.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.Routes
import com.example.test.TestViewModel
import com.example.test.network.AudioFile
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import kotlin.time.Duration.Companion.milliseconds
import androidx.core.net.toUri
import androidx.media3.session.legacy.PlaybackStateCompat

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LocalStorageTestScreen(context: Context,viewModel: TestViewModel,
                           navController: NavController){
    var txt by remember() { mutableStateOf("") }
    var txtGotten by remember { mutableStateOf("") }

    val myImage: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.placeholder)
    var result by remember { mutableStateOf<Bitmap>(myImage) }
    val loadImage  = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            result = it
        }
    }

    val audioFiles by viewModel.audioFiles.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getAudioFiles()
    }



//        CustomTextField(label = "Enter Text", _value = txt, onValueChange = {txt=it},
//            isPassword = false, icon = Icons.Default.TextFields)
//        Spacer(modifier = Modifier.padding(10.dp))
//        Button(
//            onClick = {saveText(context, txt,"test.txt")}
//        ) {
//            Text("Save Text")
//        }
//
//        Spacer(modifier = Modifier.padding(10.dp))
//        Button(
//            onClick = { txtGotten=getText(context,"test.txt") }
//        ) {
//            Text("Get Text")
//        }
//        Spacer(modifier = Modifier.padding(10.dp))
//        Text(txtGotten)
//        Spacer(modifier = Modifier.padding(10.dp))
////        GrantUserReadPermission()
////        Spacer(modifier = Modifier.padding(10.dp))
//        Button(onClick = { loadImage.launch() }) {
//            Text("Take Picture")
//        }
//
//        Image(
//                bitmap = result.asImageBitmap(),
//        contentDescription = "picture"
//        )
//
//        Spacer(modifier = Modifier.padding(10.dp))
//        Button(onClick = {saveImageToGallery(context,result,"test")}) {
//            Text("Save Image to Gallery")
//        }

        GrantUserReadPermission()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                ),
                title = {
                    Row() {
                        IconButton(
                            onClick = { navController.popBackStack()}
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back Button",
                            )

                        }
                        Text(
                            "Audio Files",
                            modifier = Modifier.padding(top = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.background(Color.White).padding(innerPadding)){
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp)){
                items(audioFiles){ index ->
                    AudioFilesCard(index, navController)
                    Spacer(modifier = Modifier.padding(10.dp))

                }
            }
        }
    }






}
@Composable
fun AudioFilesCard(audioFile: AudioFile,navController: NavController){
    val context=LocalContext.current
    val parsedUri: Uri = audioFile.uri.toUri()
    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3F3),
            contentColor = Color.Black

        ),
        modifier = Modifier.clickable(
            onClick = { navController.navigate(Routes.AudioPlayerScreen(audioFile.uri)) }
        )

    ) {
        Box(Modifier.fillMaxWidth()){
            Column(modifier = Modifier.padding(10.dp)) {

                Text(text = "Name:  ${audioFile.title}",
                    fontWeight = FontWeight.Bold)
                Text(text = "Duration:  ${audioFile.duration.milliseconds} ")
                Text(text = "Path: ${audioFile.filePath}")


            }
            IconButton(
                onClick = {
                    val intent=Intent(Intent.ACTION_SEND).apply {
                        type="audio/*"
                        putExtra(Intent.EXTRA_STREAM, parsedUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    }
                    if(intent.resolveActivity(context.packageManager)!=null){
                        context.startActivity(Intent.createChooser(intent,"Share Audio File"))

                    }

                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Play"
                )
            }
        }



    }

}

fun saveText(context: Context,text:String, fileName:String){
    val file= File(context.filesDir,fileName)
    file.writeText(text)
}

fun saveImageToGallery(context: Context, bitmap: Bitmap, filename: String) {
   val contentValue=ContentValues().apply {
       put(MediaStore.Images.Media.DISPLAY_NAME,"$filename.jpg")
       put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
       put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/test")
   }

    val uri=context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValue)

    uri?.let {
        context.contentResolver.openOutputStream(it).use { outputStream ->
            outputStream?.let { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            }
        }
    }

}



fun getText(context: Context,fileName: String):String{
    val file= File(context.filesDir,fileName)
    return (if (file.exists()) file.readText() else "not found")
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun GrantUserReadPermission(){
    //read audio permission
    var readPermissionState = rememberPermissionState(
        Manifest.permission.READ_MEDIA_AUDIO
    )
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        readPermissionState= rememberPermissionState(Manifest.permission.READ_MEDIA_AUDIO)
    }
    else{
        readPermissionState= rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (!readPermissionState.status.isGranted){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
            Button(
                onClick = {readPermissionState.launchPermissionRequest()}
            ) {
                Text("Request permission")
            }
        }
    }
    }

}


