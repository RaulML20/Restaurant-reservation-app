package com.example.androidroom.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidroom.entities.*
import com.example.androidroom.repository.ReservationRepository
import com.example.androidroom.useCase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: ReservationRepository): ViewModel(){
    val allClient: LiveData<List<ClientEntity>> = repository.allClient.asLiveData()
    val allRestaurants: LiveData<List<RestaurantEntity>> = repository.allRestaurants.asLiveData()

    fun insertCliente(client: ClientEntity) = viewModelScope.launch {
        repository.insertClient(client)
    }

    fun insertRestaurant(restaurant: RestaurantEntity) = viewModelScope.launch {
        repository.insertRestaurant(restaurant)
    }
}