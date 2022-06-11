package com.example.androidroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidroom.entities.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDAO {

    @Query("SELECT * FROM restaurants ORDER BY idR ASC")
    fun getRestaurants(): Flow<List<RestaurantEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(restaurant : RestaurantEntity)

    @Query("DELETE FROM restaurants")
    suspend fun deleteAll()
}