package com.example.test.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.test.R
import com.example.test.Routes
import com.example.test.TestViewModel
import com.example.test.UIState
import com.example.test.network.Product
import kotlinx.coroutines.flow.forEach


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.EcommerceDashboardScreen(viewModel: TestViewModel,
                                                   navController: NavController,
                                                   animatedVisibilityScope: AnimatedVisibilityScope){
    val product by viewModel.products.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    var addToFav by remember{
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        viewModel.getProducts()

    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                ),
                title = {
                    Row (modifier = Modifier.fillMaxWidth()){
                        Text("Products", fontWeight = FontWeight.Bold)
                        IconButton(
                            onClick = {
                                navController.navigate(Routes.LocalStorageTestScreen)

                            },

                            modifier = Modifier.padding(start = 160.dp)
                        ) {
                            Icon(
                                Icons.Default.AssignmentLate,
                                tint = Color.Black,
                                contentDescription = "",
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                        }
                        IconButton(
                            onClick = {
                                navController.navigate(Routes.FavoriteScreen)
                            
                            },

                            modifier = Modifier.padding(start = 5.dp)
                        ) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                tint = Color.Black,
                                contentDescription = "",
                                modifier = Modifier.padding(bottom = 20.dp)
                            )
                            Icon(
                                Icons.Default.Favorite,
                                tint = Color.Black,
                                contentDescription = "Favorite Button",
                                modifier = Modifier.padding(bottom = 20.dp)

                            )
                        }

                    }
                }
            )
        },
    ) { innerPadding ->
        when(uiState){
            UIState.Default -> {
                Column (modifier = Modifier
                    .background(color = Color.White)){
                    LottieAnimation(composition)

                }
            }
            is UIState.Error ->{

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.background(Color.White).fillMaxSize()) {
                        if ((uiState as UIState.Error).message == "404"){
                            LottieAnimation(
                                composition=rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.notfound)).value,
                            )
                        }
                        else{
                            Text((uiState as UIState.Error).message, fontSize = 20.sp,
                                color = Color.Black)
                        }

                    }


            }
            UIState.Loading -> {
            }
            is UIState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(innerPadding),
                ) {

                    itemsIndexed(product) { index, item ->
                        Box(modifier = Modifier.fillMaxSize()){
                            ProductDetailCard(product = item,navController,addToFav,viewModel,animatedVisibilityScope)

                        }
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProductDetailCard(product: Product,
                      navController: NavController, addToFav:Boolean,
                                            viewModel: TestViewModel,
                        animatedVisibilityScope: AnimatedVisibilityScope){
    var addToFavCheck by remember {
        mutableStateOf(addToFav)
    }
    val context = LocalContext.current
    val favProducts by viewModel.favorites.collectAsState()


    Box(modifier = Modifier.fillMaxSize()){

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(10.dp)) {
            Column (modifier = Modifier.padding(10.dp)) {
                Column(modifier = Modifier
                    .size(175.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF6CDBB), Color(0xFFF8F3F0)
                            )
                        )
                    )

                ) {
                    Image(
                        painter = rememberAsyncImagePainter(product.image),
                        contentDescription = product.title,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(top = 30.dp)
                            .sharedElement(
                                animatedVisibilityScope=animatedVisibilityScope,
                                sharedContentState = rememberSharedContentState(
                                    key = product.image
                                )
                            )
                    )
                }

                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.padding(5.dp)
                )
                Row (
                    modifier = Modifier.fillMaxSize().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        "$${product.price}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFFa39621),
                    )
                    Button(
                        onClick = {
                            navController.navigate(Routes.ProductDetailScreen(product.id,product.title,
                                product.image,product.price,product.description))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF353D52),
                            contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        modifier = Modifier
                            .width(44.dp)
                            .height(22.dp)

                    ) {
                        Text("Buy",fontSize = 8.sp)

                    }

                }


            }
        }

        IconButton(onClick ={
            if (addToFavCheck){
                Toast.makeText(context,"Product Already in favorites",Toast.LENGTH_SHORT).show()
            }
            else{
                viewModel.addProductToFavorite(product)

                addToFavCheck=!addToFavCheck
                Toast.makeText(context,"Added to favorites",Toast.LENGTH_SHORT).show()
            }
        },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            favProducts.forEach {
                if (it.id==product.id){
                    addToFavCheck=true
                }
            }
            Icon(Icons.Default.Favorite, contentDescription = "Favorite",
                tint =if (addToFavCheck) Color.Red else Color(0xFF967474)
            )
        }

    }



}