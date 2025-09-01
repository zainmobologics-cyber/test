package com.example.test.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.test.UIState
import com.example.test.network.Product
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.json.JSONStringer


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProductDetailScreen(title:String, price: Double, description:String, image:String
                        , navController: NavController, animatedVisibilityScope: AnimatedVisibilityScope
) {

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
                            "Details",
                            modifier = Modifier.padding(top = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            var expandDesc by remember { mutableStateOf(false) }
            var maxDesc by remember { mutableIntStateOf(4) }
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(top = 20.dp,)) {

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFE3A999)
                                    ,Color(0xFFF1D2CB)
                                )
                            )
                        )
                        .align(Alignment.CenterHorizontally)
                        .size(width = 350.dp, height = 400.dp)
                        .padding(5.dp)
                ) {
                    Image(painter = rememberAsyncImagePainter(image), contentDescription =title,
                        modifier = Modifier.size(360.dp).align(Alignment.CenterHorizontally)
                            .sharedElement(
                            animatedVisibilityScope=animatedVisibilityScope,
                            sharedContentState = rememberSharedContentState(
                                key = image
                            )
                        ))

                }

                Column(Modifier.padding(15.dp).verticalScroll(rememberScrollState())) {
                    Text(title,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.size(6.dp))


                    Text("$$price",
                        fontSize = 45.sp,
                        color = Color(0xFFC2B232),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif)

                    Spacer(modifier = Modifier.size(10.dp))
                        Text(description,
                            fontSize = 15.sp,
                            color = Color.Black,
                            maxLines = maxDesc,
                            overflow = if (!expandDesc) TextOverflow.Ellipsis else TextOverflow.Visible,
                            modifier = Modifier.clickable(
                                onClick ={expandDesc=true
                                maxDesc=100}
                            )
                            )


                    Spacer(modifier = Modifier.size(16.dp))
                    Button(
                        onClick = {
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCB7B67),
                            contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Add to Cart")
                    }
                }


            }
        }


    }
}


