package com.example.androidroom.dao

import androidx.room.*
import com.example.androidroom.entities.*
import com.example.androidroom.view.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDAO {
    @Query("SELECT * FROM reviews ORDER BY idR ASC")
    fun getReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reviews : ReviewEntity)

    @Query("DELETE FROM reviews")
    suspend fun deleteAll()

    @Query("UPDATE reviews set comentary = :comentary WHERE idR IN (:idReview)")
    suspend fun update(comentary : String, idReview : Int)

    @Transaction
    @Query("SELECT * FROM clients INNER JOIN reviews ON clients.idC = reviews.idReviewC")
    fun getClientsWithReviews(): Flow<List<ClientsWithReviews>>

    @Transaction
    @Query("SELECT * FROM restaurants INNER JOIN reviews ON restaurants.idR = reviews.idReviewR")
    fun getRestaurantsWithReviews(): Flow<List<RestaurantsWithReviews>>
}