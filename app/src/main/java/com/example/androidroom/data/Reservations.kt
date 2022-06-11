package com.example.androidroom.data

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Reservations(
    @SerializedName("numberR") var numberR: Int,
    @SerializedName("date") var date: String,
    @SerializedName("name") var name: String,
    @SerializedName("hour") var hour: Int,
    @SerializedName("minute") var minute: Int,
    @SerializedName("clientsN") var clientsN: Int,
    @SerializedName("state") var state: String,
    @SerializedName("idU") var idU : Int,
    @SerializedName("idR") var idR : Int
)
