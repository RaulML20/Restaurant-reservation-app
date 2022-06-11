package com.example.androidroom.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidroom.entities.ClientEntity
import com.example.androidroom.entities.ReservationEntity
import com.example.androidroom.entities.RestaurantEntity
import com.example.androidroom.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(private val repository: ReservationRepository): ViewModel(){

    val allReservations: LiveData<List<ReservationEntity>> = repository.allReservas.asLiveData()

    fun insertReservation(reservation : ReservationEntity) = viewModelScope.launch {
        repository.insertReservation(reservation)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAllReservations()
    }

    fun deleteReservation(numberR : Int) = viewModelScope.launch {
        repository.deleteReservation(numberR)
    }
}