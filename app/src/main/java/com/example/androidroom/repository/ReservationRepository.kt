package com.example.androidroom.repository

import androidx.annotation.WorkerThread
import com.example.androidroom.dao.*
import com.example.androidroom.data.Location
import com.example.androidroom.data.Restaurants
import com.example.androidroom.entities.*
import com.example.androidroom.webService.Retrofit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ReservationRepository @Inject constructor(private val clientDAO: ClientDAO, private val reservationDAO: ReservationDAO, private val discountDAO : DiscountDAO, private val restaurantDAO : RestaurantDAO, private val reviewDAO : ReviewDAO, private val locationDAO: LocationDAO) {


    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    /**
     *  CALLS OF THE LOCAL DATABASE
     */

    val allLocation : Flow<List<LocationEntity>> = locationDAO.getLocation()
    val allClient: Flow<List<ClientEntity>> = clientDAO.getClients()
    val allRestaurants: Flow<List<RestaurantEntity>> = restaurantDAO.getRestaurants()
    val allReservas: Flow<List<ReservationEntity>> = reservationDAO.getReservations()
    val allReviews: Flow<List<ReviewEntity>> = reviewDAO.getReviews()
    val allDiscount: Flow<List<DiscountEntity>> = discountDAO.getDiscounts()
    val allClientesWithR: Flow<List<ClientsWithReservations>> = reservationDAO.getClientsWithReservation()
    val allRestaurantsWithR: Flow<List<RestaurantsWithReservations>> = reservationDAO.getRestaurantsWithReservations()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.

    /**
     * NEW INSERT METHODS
     */

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllRestaurants() {
        restaurantDAO.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllReservations() {
        reservationDAO.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllLocation() {
        locationDAO.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertLocation(location: LocationEntity) {
        locationDAO.insert(location)
    }

    /**
     * INSERT METHODS OF LOCAL DATABASE
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertClient(client: ClientEntity) {
        clientDAO.insert(client)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertRestaurant(restaurant: RestaurantEntity) {
        restaurantDAO.insert(restaurant)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertReservation(reservas: ReservationEntity) {
        reservationDAO.insert(reservas)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertReview(reviews: ReviewEntity) {
        reviewDAO.insert(reviews)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertDiscount(discount: DiscountEntity) {
        discountDAO.insert(discount)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteReservation(numberR : Int) {
        reservationDAO.deleteReservation(numberR)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateReview(comentary : String, idReview : Int) {
        reviewDAO.update(comentary, idReview)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateDiscount(porcentage : Double, idD : Int) {
        discountDAO.update(porcentage, idD)
    }
}