package com.example.androidroom.data

import com.google.gson.annotations.SerializedName

data class Restaurants(
    @SerializedName("idU") var idU: Int,
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("TypeU") var TypeU : String,
    @SerializedName("image") var image : String,
    @SerializedName("phone") var phone : Int,
    @SerializedName("price") var price : Int,
    @SerializedName("description") var description : String,
    @SerializedName("numberT") var numberT: Int,
    @SerializedName("punctuation") var punctuation: Float,
    @SerializedName("idL") var idL : Int
)