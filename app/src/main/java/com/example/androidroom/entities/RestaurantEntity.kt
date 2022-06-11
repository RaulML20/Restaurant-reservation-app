package com.example.androidroom.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey @ColumnInfo(name = "idR") var idR: Int,
    @ColumnInfo(name = "nombreR") var nameR: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "TypeU") var TypeU: String,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "phone") var phone: Int,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "punctuation") var punctuation: Float,
    @ColumnInfo(name = "idL") var idL: Int,
    @ColumnInfo(name = "distance") var distance: Float,
)


