package com.example.test.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.test.Routes
import com.example.test.TestViewModel
import com.example.test.UIState
import com.example.test.R

@Composable
fun DashboardScreen(navController: NavController,viewModel: TestViewModel){

    val users by  viewModel.users .collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec
        .RawRes(R.raw.welcome))
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

//    viewModel.save("Hello World")
//    viewModel.load()
    val data by viewModel.data.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()) {
        LottieAnimation(composition,
            modifier = Modifier.size(250.dp))
        Button(
                onClick = {
                    viewModel.loadUser()

                }) {

                Text("Load Users")
        }

        when (uiState) {
            is UIState.Loading -> {
                    MyCircularIndicator()

            }
            is UIState.Success  -> {
                LazyColumn(){
                    items(viewModel.users.value.size){
                        Text(
                            text = users[it].name,
                        )
                    }
                }
            }
            is UIState.Error -> {
                Text("Error")
            }

        else -> Text("Press the load users button to load users")
        }

        Button(
            onClick = {
                navController.navigate(Routes.ScreenB(
                    v = listOf(1,2,3,4,5)
                ))
            }) {
            Text("Go to B")
        }

        }

}


@Composable
fun MyCircularIndicator(){
    CircularProgressIndicator(
        color = Color.Red,
        modifier = Modifier
            .size(80.dp)
    )
}