package com.example.androidroom.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidroom.dao.*
import com.example.androidroom.entities.*

@Database(entities = [ClientEntity::class, ReservationEntity::class, RestaurantEntity::class, DiscountEntity::class, ReviewEntity::class, LocationEntity::class], version = 2, exportSchema = false)
abstract class ReservationsDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDAO
    abstract fun reservationDao(): ReservationDAO
    abstract fun restaurantDao(): RestaurantDAO
    abstract fun discountDao(): DiscountDAO
    abstract fun reviewDao(): ReviewDAO
    abstract fun locationDao(): LocationDAO
}
