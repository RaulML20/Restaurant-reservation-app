package com.example.androidroom.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.fragment.app.Fragment
import com.example.androidroom.AESEncyption
import com.example.androidroom.R
import com.example.androidroom.data.Users
import com.example.androidroom.entities.ClientEntity
import com.example.androidroom.view.DataProtectionActScreen
import com.example.androidroom.view.MainHall
import com.example.androidroom.viewModel.RegisterViewModel
import com.example.androidroom.webService.Retrofit


class FragmentRegister(viewR : RegisterViewModel, context : Context) : Fragment() {
        private val viewModel = viewR

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val intent = Intent(context, DataProtectionActScreen::class.java)
            return ComposeView(requireContext()).apply {
                viewModel.allClient.observe(viewLifecycleOwner) { user ->
                    run {
                        setContent {
                            RegisterScreen(user, viewModel, requireContext(), intent)
                        }
                    }
                }
            }
        }
    }

@Composable
fun RegisterScreen(list : List<ClientEntity>, viewModel : RegisterViewModel, context : Context, intent : Intent){

    val checked = remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }


    Scaffold(backgroundColor = Color.White) {
        Column {
            Image(
                painterResource(id = R.drawable.tiger),
                contentDescription = "icon",
                modifier = Modifier
                    .padding(125.dp, 0.dp, 0.dp, 0.dp)
                    .size(110.dp)
            )
            Box(modifier = Modifier
                .height(470.dp)
                .width(400.dp)
                .padding(0.dp, 0.dp, 0.dp, 10.dp)){
                Text(
                    text = "First Name",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp, 0.dp, 0.dp, 0.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp, 22.dp, 0.dp, 0.dp)
                        .width(350.dp),
                    value = name,
                    label = { Text("name") },
                    onValueChange = { name = it },
                )
                Text(
                    text = "Last Name",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp, 95.dp, 0.dp, 0.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp, 117.dp, 0.dp, 0.dp)
                        .width(350.dp),
                    value = lastname,
                    label = { Text("last name") },
                    onValueChange = { lastname = it },
                )
                Text(
                    text = "Email",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp, 185.dp, 0.dp, 0.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp, 210.dp, 0.dp, 0.dp)
                        .width(350.dp),
                    value = email,
                    label = { Text("email") },
                    onValueChange = { email = it },
                )
                Text(
                    text = "Phone",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp, 285.dp, 0.dp, 0.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp, 310.dp, 0.dp, 0.dp)
                        .width(350.dp),
                    value = phone,
                    label = { Text("phone") },
                    onValueChange = { phone = it },
                )
                Text(
                    text = "Password",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp, 380.dp, 0.dp, 0.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(15.dp, 400.dp, 0.dp, 0.dp)
                        .width(350.dp),
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text("password") },
                    onValueChange = { password = it },
                )
            }
            Row{
                Checkbox(checked = checked.value, onCheckedChange = {
                    checked.value = it
                }, modifier = Modifier
                    .width(30.dp)
                    .padding(80.dp, 12.dp, 0.dp, 0.dp)
                    .height(30.dp))
                Text(
                    text = "Data Protection Act",
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .padding(90.dp, 15.dp, 0.dp, 0.dp)
                        .clickable {
                            activity(context)
                        }
                )
            }

            Button(
                onClick = {
                    val conditions = validations(name, password, email, lastname, phone, context, checked.value)
                    if(conditions){
                        insertUser(name, password, email, lastname, phone, list, viewModel, context)
                    }
                },
                modifier = Modifier
                    .width(340.dp)
                    .padding(40.dp, 35.dp, 0.dp, 0.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.background))
            ) {
                Text(
                    text = "Register",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                )
            }
        }
    }
}

fun activity(context : Context){
    val intent = Intent(context, MainHall::class.java)
    intent.putExtra("register","data")
    context.startActivity(intent)
}

fun insertUser(username : String, password : String, email : String, lastname : String, phone : String, list : List<ClientEntity>, viewModel : RegisterViewModel, context : Context){

    val encryptPassword = AESEncyption.encrypt(password)

    val retro = Retrofit(null, null, null, null)
    val user = ClientEntity(list.last().idC+1, username, encryptPassword.toString(), email, username, lastname, phone)
    val userOnline = Users(list.last().idC+1, username, encryptPassword.toString(), "Client", "", "", phone, 0, "", email, username, lastname, 0)
    retro.createUser(userOnline)
    viewModel.insertCliente(user)

    val intent = Intent(context, MainHall::class.java)
    intent.putExtra("register","login")
    context.startActivity(intent)
}

fun validations(name : String, password : String, email : String, lastname : String, phone : String, context : Context, checked : Boolean) : Boolean{
    if(name != "" && password != "" && email != "" && lastname != "" && phone != ""){
        val c1 = checkEmail(email)
        return if(c1 && checked){
            true
        }else{
            if(!c1){
                Toast.makeText(context, "The email is not valid", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "The data protection act must be accept", Toast.LENGTH_LONG).show()
            }
            false
        }
    }else{
        Toast.makeText(context, "The fields should not be empty", Toast.LENGTH_LONG).show()
    }
    return false
}

fun checkEmail(email : String): Boolean {
    val emailPattern  = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    if(email.matches(emailPattern.toRegex())){
        return true
    }
    return false
}