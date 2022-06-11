package com.example.androidroom.dao

import androidx.room.*
import com.example.androidroom.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DiscountDAO {

    @Query("SELECT * FROM discounts ORDER BY idD ASC")
    fun getDiscounts(): Flow<List<DiscountEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(discounts : DiscountEntity)

    @Query("DELETE FROM discounts")
    suspend fun deleteAll()

    @Query("UPDATE discounts set porcentage = :porcentage WHERE idD IN (:idD)")
    suspend fun update(porcentage : Double, idD : Int)

    @Transaction
    @Query("SELECT * FROM clients INNER JOIN discounts ON clients.idC = discounts.idDiscountC")
    fun getClientsWithDiscounts(): Flow<List<ClientsWithDiscounts>>

    @Transaction
    @Query("SELECT * FROM restaurants INNER JOIN discounts ON restaurants.idR = discounts.idDiscountR")
    fun getRestaurantsWithDiscounts(): Flow<List<RestaurantsWithDiscounts>>
}