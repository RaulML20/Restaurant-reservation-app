package com.example.androidroom.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.bumptech.glide.Glide
import com.example.androidroom.databinding.ActivityRestaurantProfileBinding
import com.example.androidroom.fragments.MapFragment
import com.example.androidroom.webService.Retrofit

class RestaurantProfile : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantProfileBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retro = Retrofit(null, null, null, null)
        retro.getLocationMap(intent.getStringExtra("title").toString(), this)
        val url = intent.getStringExtra("url")

        Glide.with(this)
            .load(url)
            .into(binding.restaurantImage)

        val idR = intent.getIntExtra("idR", 0)
        binding.title.text = intent.getStringExtra("title")
        binding.phone.text = "Phone: ${intent.getIntExtra("phone", 0)}"
        binding.description.text = intent.getStringExtra("desc")
        binding.stars.text = intent.getFloatExtra("punctuation", 0.0F).toString()
        binding.price.text = "Average price: ${intent.getIntExtra("price", 0)} €"
        binding.loc.text = "Distance: ${intent.getDoubleExtra("dis", 0.0)} km"

        val title : String = intent.getStringExtra("title").toString()
        binding.description.movementMethod = ScrollingMovementMethod()

        binding.book.setOnClickListener {
            val intent = Intent(this, Reservation::class.java)
            println(idR)
            intent.putExtra("idR", idR)
            startActivity(intent)
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, Review::class.java)
            intent.putExtra("id", idR)
            startActivity(intent)
        }

        binding.map.setOnClickListener {
            val intent = Intent(this, MapFragment::class.java)
            intent.putExtra("name", title)
            startActivity(intent)
        }
    }
}