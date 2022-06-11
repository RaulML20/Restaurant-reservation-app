package com.example.androidroom.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.androidroom.R
import com.example.androidroom.fragments.*
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.viewModel.ReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuRestaurants : AppCompatActivity() {

    private val viewModel: ReservationViewModel by viewModels()
    private val viewModelR: ReviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_restaurants)
        val navigationView2 = findViewById<BottomNavigationView>(R.id.navigationView2)

        replaceFragment(ReservationRestaurants(viewModel))

        navigationView2.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.reservation ->{
                    replaceFragment(ReservationRestaurants(viewModel))
                }
                R.id.review-> {
                    replaceFragment2(FragmentReviewList(viewModelR))
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : ReservationRestaurants){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment2(fragment : FragmentReviewList){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }
}

