package com.example.androidroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidroom.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDAO {

    @Query("SELECT * FROM location")
    fun getLocation(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: LocationEntity)

    @Query("DELETE FROM location")
    suspend fun deleteAll()
}