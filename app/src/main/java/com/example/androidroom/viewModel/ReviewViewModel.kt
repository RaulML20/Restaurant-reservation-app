package com.example.androidroom.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidroom.entities.ReservationEntity
import com.example.androidroom.entities.ReviewEntity
import com.example.androidroom.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: ReservationRepository): ViewModel(){

    val allReviews : LiveData<List<ReviewEntity>> = repository.allReviews.asLiveData()

    fun insertReview(review : ReviewEntity) = viewModelScope.launch {
        repository.insertReview(review)
    }
}