package com.example.test.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.test.R
import com.example.test.Routes
import com.example.test.TestViewModel
import com.example.test.UIState
import com.example.test.ui.CustomTextField
import kotlinx.coroutines.delay
import kotlin.math.log

@Composable
fun LoginScreen(
    viewModel: TestViewModel,
    navController: NavController
){
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context= LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login))

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = listOf(Color.White,Color.LightGray))
            )){
        Image(
            painter = painterResource(id = R.drawable.freepik),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 50.dp)
        )
        Text("Login to your account",
            fontSize = 24.sp, fontWeight = FontWeight.W400,
            modifier = Modifier.padding(start = 44.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
            CustomTextField(label = "Email", _value = email,
                onValueChange = {email=it}, isPassword = false, icon = Icons.Default.Email)
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(label = "Password", _value = password,
                onValueChange = {password=it},isPassword = true,icon = Icons.Default.Password)
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.login(email,password)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF153D8A),
                    contentColor = Color.White),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(0.84f)
            ) {
                Row {
                    when(uiState){
                        is UIState.Loading -> {
                            Spacer(modifier = Modifier.width(3.dp))
                            CircularProgressIndicator()
                        }
                        else -> {Text("Login")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text("Don't have an Account? ",
                    color = Color.Gray)
                Text("Sign Up", color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(
                        onClick = { navController.navigate(Routes.SignUpScreen)
                        viewModel.resetState()}
                    ))
            }
        }

    }
    when(uiState){
        UIState.Default -> {

        }
        is UIState.Error -> {
            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
        }
        UIState.Loading -> {

        }
        is UIState.Success -> {
            viewModel.resetState()
            navController.navigate(Routes.EcommerceDashboardScreen)

        }
    }

}


