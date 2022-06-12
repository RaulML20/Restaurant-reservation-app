package com.example.androidroom.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidroom.R
import com.example.androidroom.entities.ClientEntity
import com.example.androidroom.entities.RestaurantEntity
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.useCase.LoginUseCase
import com.example.androidroom.view.MainHall
import com.example.androidroom.view.Menu
import com.example.androidroom.view.MenuRestaurants
import com.example.androidroom.viewModel.ClientViewModel
import com.example.androidroom.viewModel.LoginViewModel
import com.example.androidroom.webService.Retrofit
import com.google.android.material.internal.ContextUtils.getActivity
import java.net.UnknownHostException

class FragmentLogin(viewL : LoginViewModel, viewC : ClientViewModel) : Fragment() {

    private val sharedPreferencesManager = SharedPreferencesManager()
    private val viewModel = viewL
    private val viewModel2 = viewC

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {

            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)

            val retro = Retrofit(null, null, viewModel2, null)
            retro.searchUsers(context)

            val userName : String = sharedPreferencesManager.getUsername(context).toString()
            val type : String = sharedPreferencesManager.getType(context).toString()

            if(userName != ""){
                if(type == "client"){
                    val int = Intent(context, Menu::class.java)
                    startActivity(int)
                }else if(type == "restaurant"){
                    val int = Intent(context, MenuRestaurants::class.java)
                    startActivity(int)
                }
            }

            viewModel.allRestaurants.observe(viewLifecycleOwner) { restaurant ->
                val restaurants = restaurant.toMutableList()
                viewModel.allClient.observe(viewLifecycleOwner) { user ->
                    val users = user.toMutableList()
                    setContent {
                        LoginScreen(users, viewModel, context, restaurants)
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(list : List<ClientEntity>, viewModel : LoginViewModel, context : Context, restaurants : List<RestaurantEntity>){

    var text by remember { mutableStateOf("")}

    var text2 by remember { mutableStateOf("")}


    Scaffold(backgroundColor = Color.White) {
        Column {
            Image(painterResource(id = R.drawable.tiger),
                contentDescription = "icon",
                modifier = Modifier
                    .padding(125.dp, 20.dp, 0.dp, 0.dp)
                    .size(140.dp)
            )
            Box(modifier = Modifier
                .height(320.dp)
                .width(400.dp)
                .padding(0.dp, 40.dp, 0.dp, 10.dp)){
                Text(
                    text = "Enter your email",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp, 0.dp, 0.dp, 0.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp, 40.dp, 0.dp, 0.dp)
                        .width(350.dp),
                    value = text,
                    label = {Text("Email")},
                    onValueChange = { text = it },
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp, 130.dp, 0.dp, 0.dp)
                        .width(350.dp),
                    value = text2,
                    label = {Text("Password")},
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { text2 = it },
                )
                Text(
                    text = "Welcome back!",
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.background),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(15.dp, 0.dp, 0.dp, 33.dp)
                )
            }
            Button(
                onClick = {println(list)
                    login(text.trim(), text2.trim(), viewModel, context, list, restaurants)},
                modifier = Modifier
                    .width(340.dp)
                    .padding(40.dp, 10.dp, 0.dp, 0.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.background))
            ) {
                Text(text = "Login",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,)
            }

            Text(
                text = "Don't have an account yet?",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(70.dp, 50.dp, 0.dp, 0.dp)
            )

            OutlinedButton(
                onClick = {
                    val intent = Intent(context, MainHall::class.java)
                    intent.putExtra("register", "register")
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .width(340.dp)
                    .padding(40.dp, 20.dp, 0.dp, 0.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30),
                border = BorderStroke(2.dp, color = colorResource(id = R.color.background))
            ) {
                Text(text = "Register",
                    color = colorResource(id = R.color.background),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,)
            }
        }
    }
}

@SuppressLint("RestrictedApi")
fun login(username : String, password : String, viewModel : LoginViewModel, context : Context, clients : List<ClientEntity>, restaurants : List<RestaurantEntity>){
    val sharedPreferencesManager = SharedPreferencesManager()

    val login = LoginUseCase(viewModel)
    val result = login.loginComprobation(context, username, password, clients, restaurants)
    if(result){
        val type : String? = sharedPreferencesManager.getType(context)
        if(type == "client"){
            val idC = login.getUserId(username, clients)

            println("IDC inicial $idC")

            sharedPreferencesManager.setIdc(context, idC)
            val int = Intent(context, Menu::class.java)
            context.startActivity(int)
            getActivity(context)?.finish()
        }else{
            val idC = login.getRestaurantId(username, restaurants)

            println("IDC inicial $idC")

            sharedPreferencesManager.setUsername(context, username)
            sharedPreferencesManager.setIdc(context, idC)
            val int = Intent(context, MenuRestaurants::class.java)
            context.startActivity(int)
            getActivity(context)?.finish()
        }
    }
}