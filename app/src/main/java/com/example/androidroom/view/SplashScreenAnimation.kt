package com.example.androidroom.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.androidroom.R
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.delay

@SuppressLint("RestrictedApi")
@Composable
fun AnimationSplashScreen(context : Context) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    Surface(modifier = Modifier
        .size(400.dp)
        .scale(scale.value),
        color = Color.White,
    ) {
        Column(modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom) {

            Image(painter = painterResource(id = R.drawable.tiger),
                contentDescription = "book-it icon",
                modifier = Modifier.size(200.dp).align(alignment = Alignment.CenterHorizontally))
        }

    }

    LaunchedEffect(key1 = true, block = {
        scale.animateTo(targetValue = 1.1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            ))
        delay(1000L)
        val intent = Intent(context, MainHall::class.java)
        context.startActivity(intent)
    })
}