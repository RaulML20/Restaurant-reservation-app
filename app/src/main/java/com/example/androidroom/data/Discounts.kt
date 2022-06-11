package com.example.androidroom.data

import com.google.gson.annotations.SerializedName

data class Discounts(
    @SerializedName("idD") var idD: Int,
    @SerializedName("porcentage") var porcentage: Double,
    @SerializedName("idR") var idR: Int
)