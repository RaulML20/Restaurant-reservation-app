package com.example.androidroom.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.androidroom.R
import com.example.androidroom.fragments.FragmentUserProfile
import com.example.androidroom.fragments.Reservations
import com.example.androidroom.fragments.RestaurantListFragment
import com.example.androidroom.viewModel.MainMenuRestaurantViewModel
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.viewModel.ReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Menu : AppCompatActivity() {
    private val viewModel: MainMenuRestaurantViewModel by viewModels()
    private val viewModel2: ReservationViewModel by viewModels()
    private val viewModel3: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigationView)

        replaceFragment(RestaurantListFragment(viewModel, viewModel2))

        navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main ->{
                    replaceFragment(RestaurantListFragment(viewModel, viewModel2))
                }
                R.id.reservation ->{
                    replaceFragment2(Reservations(viewModel, viewModel2))
                }
                R.id.profile -> {
                    replaceFragment3(FragmentUserProfile(viewModel2, viewModel3))
                }
                else ->{
                    replaceFragment(RestaurantListFragment(viewModel, viewModel2))
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : RestaurantListFragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment2(fragment : Reservations){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment3(fragment : FragmentUserProfile){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }
}