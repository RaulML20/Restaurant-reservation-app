package com.example.androidroom.viewModel

import androidx.lifecycle.*
import com.example.androidroom.entities.*
import com.example.androidroom.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val repository: ReservationRepository) : ViewModel() {

    val allClient: LiveData<List<ClientEntity>> = repository.allClient.asLiveData()
    val getClientsWithReservas: LiveData<List<ClientsWithReservations>> = repository.allClientesWithR.asLiveData()
    val getRestaurantsWithReservas: LiveData<List<RestaurantsWithReservations>> = repository.allRestaurantsWithR.asLiveData()

    fun insertUser(user : ClientEntity) = viewModelScope.launch{
        repository.insertClient(user)
    }

    fun deleteReservation(numberR : Int) = viewModelScope.launch{
        repository.deleteReservation(numberR)
    }
}