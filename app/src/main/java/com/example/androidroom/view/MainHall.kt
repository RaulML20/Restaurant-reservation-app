package com.example.androidroom.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.androidroom.R
import com.example.androidroom.fragments.FragmentLogin
import com.example.androidroom.fragments.FragmentRegister
import com.example.androidroom.viewModel.ClientViewModel
import com.example.androidroom.viewModel.LoginViewModel
import com.example.androidroom.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainHall : AppCompatActivity() {

    private val viewModel : LoginViewModel by viewModels()
    private val viewModel2 : ClientViewModel by viewModels()
    private val viewModel3 : RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hall)

        val register = intent.getStringExtra("register")

        if(register.equals("register")){
            replaceFragment2()
        }else if(register.equals("data")){
            replaceFragment3(DataProtectionActScreen())
        }else{
            replaceFragment(FragmentLogin(viewModel, viewModel2))
        }
    }

    private fun replaceFragment(fragment : FragmentLogin){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment3(fragment : DataProtectionActScreen){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    fun replaceFragment2(){
        fragment2(FragmentRegister(viewModel3, this))
    }

    private fun fragment2(fragment : FragmentRegister){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }
}