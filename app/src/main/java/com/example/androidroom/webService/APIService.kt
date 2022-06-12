package com.example.androidroom.webService

import com.example.androidroom.data.*
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIService {
    /**
     * NEW CALLS
     */
    @GET
    suspend fun getDataFromRestaurants(@Url url:String): Response<List<Restaurants>>

    @GET
    suspend fun getDataFromRestaurantsTable(@Url url:String): Response<List<Restaurants>>

    @GET
    suspend fun getRestaurantLocation(@Url url:String): Response<List<Location>>

    @FormUrlEncoded
    @POST("/updateReservation.php")
    fun updateReservation(
        @Field("numberR") numberR : Int,
        @Field("state") state : Boolean,
    ): Call<Reservations?>?

    /*@FormUrlEncoded
    @POST("/getLocation.php")
    fun getRestaurantLocation(
        @Field("idL") idL : Int
    ): Call<Location>*/

    /**
     * OLD CALLS
     */
    @GET
    suspend fun getDataFromUsers(@Url url:String): Response<List<Users>>

    @GET
    suspend fun getDataFromReservations(@Url url:String): Response<List<Reservations>>

    @GET
    suspend fun getDataFromReviews(@Url url:String): Response<List<Reviews>>

    @GET
    suspend fun getDataFromDiscounts(@Url url:String): Response<List<Discounts>>

    @FormUrlEncoded
    @POST("/users.php")
    fun createUser(
        @Field("idU") idU: Int,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("TypeU") TypeU: String,
        @Field("image") image: String,
        @Field("geopos") geopos: String,
        @Field("phone") phone: String,
        @Field("price") price: Int,
        @Field("description") description: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("lastname") lastname: String,
        @Field("idL") idL: Int
    ): Call<Users?>?

    @FormUrlEncoded
    @POST("/updatePunctuation.php")
    fun updatePunctuation(
        @Field("punctuation") punctuation : Float,
        @Field("idU") idU : Int,
    ): Call<Users?>?

    @FormUrlEncoded
    @POST("/updateRestaurantTable.php")
    fun updateRestaurantTable(
        @Field("numberT") numberT : Int,
        @Field("idU") idU : Int,
    ): Call<Users?>?

    @FormUrlEncoded
    @POST("/reservations.php")
    fun createReservation(
        @Field("numberR") numberR: Int,
        @Field("date") date: String,
        @Field("name") name: String,
        @Field("hour") hour: Int,
        @Field("minute") minute: Int,
        @Field("clientsN") clientsN: Int,
        @Field("state") state: String,
        @Field("idU") idU: Int,
        @Field("idR") idR: Int
    ): Call<Reservations?>?

    @FormUrlEncoded
    @POST("/reviews.php")
    fun createReview(
        @Field("idReview") idReview : Int,
        @Field("email") email : String,
        @Field("comentary") comentary : String,
        @Field("punctuation") punctuation : Int,
        @Field("idU") idU: Int,
        @Field("idR") idR: Int
    ): Call<Reviews?>?

    @FormUrlEncoded
    @POST("/deleteReservation.php")
    fun deleteReservation(
        @Field("numberR") numberR : Int,
    ): Call<Reservations?>?

    @FormUrlEncoded
    @POST("/updateReview.php")
    fun updateReview(
        @Field("comentary") comentary : String,
        @Field("idReview") idReview : Int
    ): Call<Reviews?>?

    @FormUrlEncoded
    @POST("/discounts.php")
    fun createDiscount(
        @Field("idD") idD: Int,
        @Field("porcentage") porcentage: Double,
        @Field("idR") idR: Int
    ): Call<Discounts?>?

    @FormUrlEncoded
    @POST("/updateDiscount.php")
    fun updateDiscount(
        @Field("porcentage") porcentage: Double,
        @Field("idD") idD: Int,
    ): Call<Discounts?>?
}