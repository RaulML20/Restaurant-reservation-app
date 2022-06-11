package com.example.androidroom.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.androidroom.R
import com.example.androidroom.databinding.ActivityReviewBinding
import com.example.androidroom.viewModel.ReviewViewModel
import com.example.androidroom.webService.Retrofit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Review : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val viewModel: ReviewViewModel by viewModels()

    private lateinit var binding: ActivityReviewBinding
    private var stars : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idReviewR = intent.getIntExtra("id", 0)

        binding.stars.onItemSelectedListener = this

        ArrayAdapter.createFromResource(
            this,
            R.array.stars,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.stars.adapter = adapter
        }

        binding.review.setOnClickListener {
            val retrofit = Retrofit(null, null, null, viewModel)
            retrofit.insertReview(this, binding.comentary.text.toString(), stars, idReviewR)
            finish()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p2) {
            0 -> {
                stars = 1
            }
            1 -> {
                stars = 2
            }
            2 -> {
                stars = 3
            }
            3 -> {
                stars = 4
            }
            4 -> {
                stars = 5
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        stars = 1
    }
}