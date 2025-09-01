package com.example.test.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.test.Routes

@Composable
fun UserDetailScreen(x:List<Int>, navController: NavController){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()) {
        x.forEach { i ->
            Text(text = i.toString())
        }
        Button(
            onClick = {
                navController.navigate(Routes.ScreenA)
            }) {
            Text("Back to Dashboard")
        }
    }

}