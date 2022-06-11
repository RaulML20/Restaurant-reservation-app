package com.example.androidroom.data

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("idU") var idU: Int,
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("TypeU") var TypeU: String,
    @SerializedName("image:") var image: String,
    @SerializedName("geopos") var geopos: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("price") var price: Int,
    @SerializedName("description") var description: String,
    @SerializedName("email") var email: String,
    @SerializedName("name") var name: String,
    @SerializedName("lastname") var lastname: String,
    @SerializedName("idL") var idL: Int
    )
