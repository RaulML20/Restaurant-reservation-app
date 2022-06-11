package com.example.androidroom.viewModel

import androidx.lifecycle.*
import com.example.androidroom.entities.LocationEntity
import com.example.androidroom.entities.RestaurantEntity
import com.example.androidroom.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuRestaurantViewModel @Inject constructor(private val repository: ReservationRepository): ViewModel(){

    val allRestaurant: LiveData<List<RestaurantEntity>> = repository.allRestaurants.asLiveData()
    val allLocation: LiveData<List<LocationEntity>> = repository.allLocation.asLiveData()

    fun insertRestaurant(restaurant: RestaurantEntity) = viewModelScope.launch {
        repository.insertRestaurant(restaurant)
    }

    fun insertLocation(location: LocationEntity) = viewModelScope.launch {
        repository.insertLocation(location)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAllRestaurants()
    }

    fun deleteAllLocations() = viewModelScope.launch {
        repository.deleteAllLocation()
    }
}