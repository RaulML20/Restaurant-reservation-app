package com.example.androidroom.dao

import androidx.room.*
import com.example.androidroom.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDAO {

    @Query("SELECT * FROM reservations ORDER BY numberR ASC")
    fun getReservations(): Flow<List<ReservationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reservas : ReservationEntity)

    @Query("DELETE FROM reservations WHERE numberR IN (:numberR)")
    suspend fun deleteReservation(numberR : Int)

    @Query("DELETE FROM reservations")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM clients INNER JOIN reservations ON clients.idC = reservations.idCreatorC")
    fun getClientsWithReservation(): Flow<List<ClientsWithReservations>>

    @Transaction
    @Query("SELECT * FROM reservations INNER JOIN restaurants ON restaurants.idR = reservations.idCreatorR")
    fun getRestaurantsWithReservations(): Flow<List<RestaurantsWithReservations>>
}