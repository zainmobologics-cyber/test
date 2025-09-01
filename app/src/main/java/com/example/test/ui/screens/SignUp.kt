package com.example.test.ui.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.test.R
import com.example.test.Routes
import com.example.test.TestViewModel
import com.example.test.UIState
import com.example.test.ui.CustomTextField

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: TestViewModel
){
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context= LocalContext.current

    when(uiState){
        UIState.Default->{
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.verticalGradient(colors = listOf(Color.White,Color.LightGray))
                    )
            ){
                Image(
                    painter = painterResource(id = R.drawable.freepik),
                    contentDescription = "Logo",
                    modifier = Modifier.align(Alignment.CenterHorizontally).
                    padding(top = 50.dp)
                )
                Text("Create your account",
                    fontSize = 25.sp, fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(start = 48.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                        .fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextField(label = "Email", _value = email,
                        onValueChange = {email=it},isPassword = false, icon = Icons.Default.Email
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextField(label = "Password", _value = password,
                        onValueChange = {password=it},isPassword = true, icon = Icons.Default.Password)
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextField(label = "Confirm Password", _value = confirmPassword,
                        isPassword = true, onValueChange = {confirmPassword=it}, icon = Icons.Default.Password)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            if (password==confirmPassword){
                                viewModel.signUp(email,password)
                                when(uiState){
                                    is UIState.Success -> {
                                        Toast.makeText(context,"Sign Up Successful",
                                            Toast.LENGTH_LONG).show()
                                        viewModel.resetState()
                                    }
                                    else -> {
                                        Toast.makeText(context,"Error Occurred",
                                            Toast.LENGTH_LONG).show()
                                    }
                                }


                            }else {
                                Toast.makeText(context,"Passwords do not match",
                                    Toast.LENGTH_LONG).show()

                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF153D8A),
                            contentColor = Color.White),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(50.dp)
                            .fillMaxWidth(0.84f)            ) {
                        Row {
                            when(uiState){
                                is UIState.Loading -> {
                                    Spacer(modifier = Modifier.width(3.dp))
                                    CircularProgressIndicator()
                                }
                                else -> {Text("SignUp")
                                }
                            }
                        }
                    }

                }

            }
        }
        is UIState.Error -> {
        }
        UIState.Loading -> {
            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
        }
        is UIState.Success -> {
            Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            navController.navigate(Routes.LoginScreen)

        }
    }
}

