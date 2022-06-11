package com.example.androidroom.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidroom.entities.*
import com.example.androidroom.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(private val repository: ReservationRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allDiscounts: LiveData<List<DiscountEntity>> = repository.allDiscount.asLiveData()
    //val allRestaurant: LiveData<List<RestaurantEntity>> = repository.allRestaurants.asLiveData()

    fun insertDiscount(discount : DiscountEntity) = viewModelScope.launch {
        repository.insertDiscount(discount)
    }

    fun updateDiscount(porcentage : Double, idD : Int) = viewModelScope.launch{
        repository.updateDiscount(porcentage, idD)
    }
}