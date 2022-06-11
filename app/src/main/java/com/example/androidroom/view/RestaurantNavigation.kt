package com.example.androidroom.view

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RestaurantNavigation(context : Context) {
    val navController =  rememberNavController()

    NavHost(navController = navController, startDestination = RestaurantScreens.SplashScreen.name){
        composable(RestaurantScreens.SplashScreen.name){
            AnimationSplashScreen(context)
        }
    }
}