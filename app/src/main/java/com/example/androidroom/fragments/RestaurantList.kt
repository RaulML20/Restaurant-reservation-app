package com.example.androidroom.fragments

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import com.example.androidroom.entities.RestaurantEntity
import com.example.androidroom.view.RestaurantProfile
import com.example.androidroom.viewModel.MainMenuRestaurantViewModel
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.webService.Retrofit
import com.example.jetpackui.ui.theme.JetpackUITheme
import com.example.jetpackui.ui.theme.ShimmerColorShades
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.lang.Exception
import kotlin.math.roundToInt

class RestaurantList(restaurant: List<RestaurantEntity>, viewM: MainMenuRestaurantViewModel, viewR : ReservationViewModel) : Fragment() {

    private val viewModel = viewM
    private val viewModel2 = viewR
    private val restaurantList = restaurant
    //val intent = Intent(context, RestaurantProfile::class.java)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            if(restaurantList.isEmpty()){
                setContent {
                    Surface(color = MaterialTheme.colors.background) {
                        LazyColumn {
                            repeat(5) {
                                item {
                                    ShimmerAnimation()
                                }
                            }
                        }
                    }
                }
            }else{
                setContent {
                    JetpackUITheme{
                        startList(restaurantList)
                    }
                }
            }
        }
    }

    fun isOnlineNet(): Boolean {
        try {
            val p = Runtime.getRuntime().exec("ping -c 1 www.google.es")
            val reachable = p.waitFor()
            return reachable == 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    @Composable
    fun ShimmerItem(
        brush: Brush
    ) {
        // Column composable containing spacer shaped like a rectangle,
        // set the [background]'s [brush] with the brush receiving from [ShimmerAnimation]
        // Composable which is the Animation you are gonna create.
        Card(elevation = 4.dp, modifier = Modifier
            .padding(18.dp, 0.dp, 18.dp, 10.dp).background(brush = brush)
            //.clickable(onClick = { startActivity(intent) }), shape = RoundedCornerShape(16.dp)
        ){
            Column {
                Row{
                    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = (brush),
                            contentDescription = "Restaurant image",
                            modifier = Modifier
                                .fillMaxSize()
                                .height(170.dp).background(brush = brush),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
                Row{
                    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier
                        .padding(10.dp, 10.dp, 10.dp, 5.dp)
                        .fillMaxSize()) {
                        Text(
                            text = "",
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier
                                .align(Alignment.BottomStart).background(brush = brush)
                        )
                        Text(
                            text = "",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .align(Alignment.BottomEnd).background(brush = brush)
                        )
                    }
                }
                Row {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp, 5.dp, 10.dp, 10.dp)) {
                        Text(
                            text = "",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .align(Alignment.BottomStart).background(brush = brush)
                        )
                        Text(
                            text = "",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .align(Alignment.BottomEnd).background(brush = brush)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ShimmerAnimation(
    ) {
        val transition = rememberInfiniteTransition()
        val translateAnim by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            )
        )
        val brush = Brush.linearGradient(
            colors = ShimmerColorShades,
            start = Offset(10f, 10f),
            end = Offset(translateAnim, translateAnim)
        )

        ShimmerItem(brush = brush)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun showRestaurantList2(items: RestaurantEntity){

        val distance2 = (items.distance * 100.0).roundToInt() / 100.0

        Card(elevation = 4.dp, modifier = Modifier
            .padding(18.dp, 0.dp, 18.dp, 10.dp).clickable {
                val intent = Intent(context, RestaurantProfile::class.java)
                intent.putExtra("title",items.nameR)
                intent.putExtra("phone",items.phone)
                intent.putExtra("desc",items.description)
                intent.putExtra("price",items.price)
                intent.putExtra("dis", distance2)
                intent.putExtra("punctuation", items.punctuation)
                intent.putExtra("idR",items.idR)
                intent.putExtra("url",items.image)
                println("Distancia: "+distance2)
                startActivity(intent)
            }
            //.clickable(onClick = { startActivity(intent) }), shape = RoundedCornerShape(16.dp)
        ){
            Column {
                Row{
                    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = (items.image),
                            contentDescription = "Restaurant image",
                            modifier = Modifier.fillMaxSize().height(196.dp),
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
                Row{
                    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 5.dp).fillMaxSize()) {
                        Text(
                            text = items.nameR,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                        Text(
                            text = items.punctuation.toString(),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                }
                Row {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(10.dp, 5.dp, 10.dp, 10.dp)) {
                        Text(
                            text = "Average price: ${items.price}€",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                        Text(
                            text = "${distance2}km",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun startList(restaurantList: List<RestaurantEntity>){

        var refreshing by remember { mutableStateOf(false) }
        LaunchedEffect(refreshing) {
            if (refreshing) {
                delay(1000)
                refreshing = false
            }
        }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true
                if(isOnlineNet()) {
                    val retro = Retrofit(viewModel, viewModel2, null, null)
                    context?.let { retro.getRestaurants(it) }
                }
            }
        ){
            LazyColumn(modifier = Modifier
                .height(870.dp)
                .padding(0.dp, 70.dp, 0.dp, 0.dp)){
                items(restaurantList) { items ->
                    showRestaurantList2(items)
                }
            }
        }
    }
}