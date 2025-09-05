package com.example.test

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.test.Routes.*
import com.example.test.network.TestWorkManager
import com.example.test.network.room.dao.FavoriteDatabase
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
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FavoriteDatabase::class.java,
            "favorites.db"
        ).build()
    }
    private lateinit var viewModel: TestViewModel
    private lateinit var workManager: WorkManager

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        workManager= WorkManager.getInstance(this)
        val audioData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }

        val startDestination = if (intent?.action == Intent.ACTION_SEND) {
            if (audioData != null) {
                Routes.AudioPlayerScreen(audioData.toString())
            } else {
                Routes.LoginScreen
            }
        } else {
            Routes.LoginScreen
        }
        viewModel = ViewModelProvider(
            this,
            TestViewModelFactory(Repo(this, db.productDao()))
        )[TestViewModel::class.java]
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        startAudioWorker(this)

        enableEdgeToEdge()
        setContent {
            TestTheme {
                val navController = rememberNavController()

                SharedTransitionLayout {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    )
                    {
                        composable<LoginScreen> {
                            LoginScreen(viewModel, navController)
                        }
                        composable<EcommerceDashboardScreen> {
                            EcommerceDashboardScreen(
                                viewModel,
                                navController,
                                animatedVisibilityScope = this
                            )
                        }
                        composable<ProductDetailScreen> {
                            val args = it.toRoute<ProductDetailScreen>()
                            ProductDetailScreen(
                                title = args.title,
                                price = args.price,
                                description = args.description,
                                image = args.image,
                                navController,
                                animatedVisibilityScope = this
                            )
                        }

                        composable<SignUpScreen> {
                            SignUpScreen(navController, viewModel)
                        }
                        composable<ScreenA> {
                            DashboardScreen(navController, viewModel)
                        }
                        composable<ScreenB> {
                            val args = it.toRoute<ScreenB>()
                            UserDetailScreen(x = args.v, navController)
                        }
                        composable<FavoriteScreen> {
                            FavoriteScreen(navController, viewModel)
                        }
                        composable<LocalStorageTestScreen>
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                LocalStorageTestScreen(
                                    context = applicationContext,
                                    viewModel,
                                    navController
                                )
                            }
                        }

                        composable<AudioPlayerScreen> {

                            val args = it.toRoute<AudioPlayerScreen>()
                            AudioPlayerScreen(audioUri = args.audioUri)
                        }
                    }
                }
            }
        }
    }

//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        Log.d("Intent Check","New Intent launched" )
//
//        setIntent(intent )
//
//    }


    @Suppress("UNCHECKED_CAST")
    class TestViewModelFactory(private val repo: Repo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TestViewModel(repo) as T
        }
    }

    fun startAudioWorker(context: Context) {
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val workerBuilder = PeriodicWorkRequestBuilder<TestWorkManager>(20, TimeUnit.SECONDS)
            .setConstraints(constraint)
            .build()
        workManager.enqueue(workerBuilder)
    }

}
