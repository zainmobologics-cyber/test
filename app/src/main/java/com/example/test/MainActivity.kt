package com.example.test

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.example.test.network.AudioService
import com.example.test.network.Product
import com.example.test.network.room.dao.FavoriteDatabase
import com.example.test.network.room.dao.ProductDao
import com.example.test.ui.screens.AudioPlayerScreen
import com.example.test.ui.screens.DashboardScreen
import com.example.test.ui.screens.EcommerceDashboardScreen
import com.example.test.ui.screens.FavoriteScreen
import com.example.test.ui.screens.LocalStorageTestScreen
import com.example.test.ui.screens.LoginScreen
import com.example.test.ui.screens.ProductDetailScreen
import com.example.test.ui.screens.SignUpScreen
import com.example.test.ui.screens.UserDetailScreen
import com.example.test.ui.theme.TestTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FavoriteDatabase::class.java,
            "favorites.db"
        ).build()
    }
    private lateinit var viewModel: TestViewModel

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {


        viewModel=ViewModelProvider(this,TestViewModelFactory(Repo(this,db.productDao())))[TestViewModel::class.java]
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        enableEdgeToEdge()
        setContent {


            TestTheme {
                val navController = rememberNavController()
                SharedTransitionLayout {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LoginScreen
                    ) {
                        composable<Routes.LoginScreen>{
                            LoginScreen(viewModel, navController)
                        }
                        composable<Routes.EcommerceDashboardScreen>{
                            EcommerceDashboardScreen(viewModel,navController, animatedVisibilityScope =this )
                        }
                        composable <Routes.ProductDetailScreen>{
                            val args=it.toRoute<Routes.ProductDetailScreen>()
                            ProductDetailScreen(title = args.title,price = args.price,description = args.description,
                                image = args.image,navController, animatedVisibilityScope = this)
                        }

                        composable<Routes.SignUpScreen>{
                            SignUpScreen(navController,viewModel)
                        }
                        composable<Routes.ScreenA>{
                            DashboardScreen(navController,viewModel)
                        }
                        composable <Routes.ScreenB>{
                            val args=it.toRoute<Routes.ScreenB>()
                            UserDetailScreen(x = args.v,navController)
                        }
                        composable <Routes.FavoriteScreen>{
                            FavoriteScreen(navController,viewModel)
                        }
                        composable <Routes.LocalStorageTestScreen>
                        {
                            LocalStorageTestScreen(context = applicationContext, viewModel,navController)
                        }

                        composable<Routes.AudioPlayerScreen> {
//                           val intent= Intent(applicationContext, AudioService::class.java).also {
//                                it.action= AudioService.Actions.START.toString()
//                                startService(it)
//                            }
                            val args = it.toRoute<Routes.AudioPlayerScreen>()
                            AudioPlayerScreen(audioUri = args.audioUri  )
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class TestViewModelFactory(private val repo: Repo): ViewModelProvider.Factory{
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return TestViewModel(repo) as T
        }
    }

}






