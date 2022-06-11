package com.example.androidroom.data

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("idL") var idL: Int,
    @SerializedName("longitude") var longitude: String,
    @SerializedName("latitude") var latitude: String
)