package com.example.androidroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidroom.entities.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDAO {

    @Query("SELECT * FROM clients ORDER BY idC ASC")
    fun getClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE username IN (:userName) ORDER BY idC ASC")
    fun getClientLogin(userName : String): Flow<ClientEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(client : ClientEntity)

    @Query("DELETE FROM clients")
    suspend fun deleteAll()
}