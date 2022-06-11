package com.example.androidroom.data

import com.google.gson.annotations.SerializedName

data class Reviews(
    @SerializedName("idReview") var idReview: Int,
    @SerializedName("email") var email: String?,
    @SerializedName("comentary") var comentary: String,
    @SerializedName("punctuation") var punctuation: Int,
    @SerializedName("idU") var idU: Int,
    @SerializedName("idR") var idR: Int
)