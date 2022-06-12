package com.example.androidroom.webService

import android.util.Log
import com.example.androidroom.data.Discounts
import com.example.androidroom.data.Reservations
import com.example.androidroom.data.Reviews
import com.example.androidroom.data.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.converter.gson.GsonConverterFactory

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.androidroom.*
import com.example.androidroom.data.*
import com.example.androidroom.data.Location
import com.example.androidroom.entities.ClientEntity
import com.example.androidroom.entities.ReservationEntity
import com.example.androidroom.entities.RestaurantEntity
import com.example.androidroom.entities.ReviewEntity
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.viewModel.ClientViewModel
import com.example.androidroom.viewModel.MainMenuRestaurantViewModel
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.viewModel.ReviewViewModel
import retrofit2.*
import retrofit2.Retrofit
import java.io.IOException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class Retrofit @Inject constructor(private val viewModel: MainMenuRestaurantViewModel?, private val viewModelR: ReservationViewModel?, private val viewModelU : ClientViewModel?, private val viewModelRe : ReviewViewModel?){

    /**
     * NEW CALLS
     */
    private val sharedPreferencesManager = SharedPreferencesManager()

    fun insertReview(context : Context, comentary : String, stars : Int, idReviewR : Int){
        val sharedPreferencesManager = SharedPreferencesManager()
        updateStars(stars, idReviewR, context)

        searchReviews(object : CallBack6 {
            override fun onSuccess(result: List<Reviews>?) {
                val idC = sharedPreferencesManager.getIdC(context)
                val reserveOnline = Reviews((result?.size ?: 0) +1, sharedPreferencesManager.getEmail(context), comentary, stars, idC, idReviewR)
                val review = ReviewEntity((result?.size ?: 0) +1, comentary, stars, idC, idReviewR)
                viewModelRe?.insertReview(review)
                createReview(reserveOnline)
            }
        }, context)
    }

   fun getRestaurantsTable(callBack7: CallBack7, con : Context) {
       CoroutineScope(Dispatchers.IO).launch {
           try{
               val call = getRetrofit().create(APIService::class.java).getDataFromRestaurantsTable(query4)
               call.body()?.let { callBack7.onSuccess(it) }
           }catch (e : UnknownHostException) {
               e.printStackTrace()
               printInternetError(con)
           }catch(ioe: IOException){
               ioe.printStackTrace()
               printInternetError(con)
           }catch (he: HttpException){
               he.printStackTrace()
               printInternetError(con)
           }
       }
    }

    fun getRestaurants2(con : Context) {
        viewModel?.deleteAll()
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val call = getRetrofit().create(APIService::class.java).getDataFromRestaurants(query4)
                call.body()?.forEach { restaurant ->
                    val r1 = RestaurantEntity(
                        restaurant.idU,
                        restaurant.username,
                        restaurant.password,
                        restaurant.TypeU,
                        restaurant.image,
                        restaurant.phone,
                        restaurant.price,
                        restaurant.description,
                        restaurant.punctuation,
                        restaurant.idL,
                        0.0F
                    )
                    viewModel?.insertRestaurant(r1)
                }
            }catch (e : UnknownHostException) {
                e.printStackTrace()
                printInternetError(con)
            }catch(ioe: IOException){
                ioe.printStackTrace()
                printInternetError(con)
            }catch (he: HttpException){
                he.printStackTrace()
                printInternetError(con)
            }
        }
    }

    fun updateStars(stars : Int, idR : Int, context : Context){
        var starsR = 0.0F
        var number = 1.0F
        searchReviews(object : CallBack6 {
            override fun onSuccess(result: List<Reviews>?) {
                result?.forEach { reviews ->
                    if(reviews.idR == idR){
                        number++
                        starsR += reviews.punctuation
                    }
                }
                starsR += stars
                updatePunctuation(starsR/number, idR)
            }
        }, context)
    }

    fun updatePunctuation(stars : Float, idR : Int){
        val retrofit = getRetrofit().create(APIService::class.java)
        val call = retrofit.updatePunctuation(stars, idR)
        call?.enqueue(object : Callback<Users?> {
            override fun onResponse(
                call: Call<Users?>,
                response: Response<Users?>
            ) {}
            override fun onFailure(call: Call<Users?>, t: Throwable) {
                call.cancel()
            }
        })
    }

    fun createReview(reserve : Reviews){
        val retrofit = getRetrofit().create(APIService::class.java)
        val call = reserve.email?.let { retrofit.createReview(reserve.idReview, it, reserve.comentary, reserve.punctuation, reserve.idU, reserve.idR) }
        call?.enqueue(object : Callback<Reviews?> {
            override fun onResponse(
                call: Call<Reviews?>,
                response: Response<Reviews?>
            ) {}
            override fun onFailure(call: Call<Reviews?>, t: Throwable) {
                call.cancel()
            }
        })
    }

    fun searchReviews(callback : CallBack6, con : Context){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val call = getRetrofit().create(APIService::class.java).getDataFromReviews(query3)
                callback.onSuccess(call.body())
            }catch (e : UnknownHostException) {
                e.printStackTrace()
                printInternetError(con)
            }catch(ioe: IOException){
                ioe.printStackTrace()
                printInternetError(con)
            }catch (he: HttpException){
                he.printStackTrace()
                printInternetError(con)
            }
        }
    }

    fun getLocationMap(username : String, con : Context){
        val locationManager: LocationManager = con.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(con,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                locationManager.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, con.mainExecutor
                ) { loc ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val call = getRetrofit().create(APIService::class.java).getDataFromRestaurants(query4)
                        call.body()?.forEach { restaurant ->
                            getRestaurantLocation(object : CallBack3 {
                                @RequiresApi(Build.VERSION_CODES.P)
                                override fun onSuccess(result: List<Location>) {
                                    result.forEach {
                                        if (restaurant.idL == it.idL) {
                                            if(restaurant.username == username){
                                                sharedPreferencesManager.setLongitude(con, it.longitude)
                                                sharedPreferencesManager.setLatitude(con, it.latitude)
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    fun getRestaurants(con : Context) {
        viewModel?.deleteAll()

        val locationManager: LocationManager = con.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(con,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                locationManager.getCurrentLocation(LocationManager.NETWORK_PROVIDER, null, con.mainExecutor
                ) { loc ->
                    val locationA = android.location.Location("point A")
                    val locationB = android.location.Location("point B")

                    CoroutineScope(Dispatchers.IO).launch {
                        try{
                            val call = getRetrofit().create(APIService::class.java).getDataFromRestaurants(query4)
                            call.body()?.forEach { restaurant ->
                                getRestaurantLocation(object : CallBack3 {
                                    @RequiresApi(Build.VERSION_CODES.P)
                                    override fun onSuccess(result: List<Location>) {
                                        result.forEach {
                                            if (restaurant.idL == it.idL) {
                                                println(restaurant)
                                                locationA.longitude = loc.longitude//-8.405903003794789,
                                                locationA.latitude = loc.latitude//43.355769086082134
                                                locationB.longitude = it.longitude.toDouble()
                                                locationB.latitude = it.latitude.toDouble()

                                                val distance = locationA.distanceTo(locationB) / 1000
                                                val r1 = RestaurantEntity(
                                                    restaurant.idU,
                                                    restaurant.username,
                                                    restaurant.password,
                                                    restaurant.TypeU,
                                                    restaurant.image,
                                                    restaurant.phone,
                                                    restaurant.price,
                                                    restaurant.description,
                                                    restaurant.punctuation,
                                                    restaurant.idL,
                                                    distance
                                                )
                                                viewModel?.insertRestaurant(r1)
                                            }
                                        }
                                    }
                                })
                            }
                        }catch (e : UnknownHostException) {
                            e.printStackTrace()
                            printInternetError(con)
                        }catch(ioe: IOException){
                            ioe.printStackTrace()
                            printInternetError(con)
                        }catch (he: HttpException){
                            he.printStackTrace()
                            printInternetError(con)
                        }
                    }
                }
            }
        }
    }

    fun updateReservation(numberR : Int, state : Boolean, date : String, clientsN: Int, con : Context, idR : Int){
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateT = df.format(c)

        if(date == dateT){
            getRestaurantsTable(object : CallBack7 {
                override fun onSuccess(result: List<Restaurants>) {
                    val restaurant = result.find { it.idU == idR }
                    val numberT = restaurant?.numberT
                    val calculate = numberT?.plus(clientsN)
                    if (calculate != null) {
                        updateRestaurantTableDay(object : CallBack5 {
                            override fun onSuccess() {
                                viewModelR?.deleteReservation(numberR)
                                val retrofit = getRetrofit().create(APIService::class.java)
                                val call = retrofit.updateReservation(numberR, state)
                                call?.enqueue(object : Callback<Reservations?> {
                                    override fun onResponse(
                                        call: Call<Reservations?>,
                                        response: Response<Reservations?>
                                    ) {}
                                    override fun onFailure(call: Call<Reservations?>, t: Throwable) {
                                        call.cancel()
                                    }
                                })
                            }
                        }, calculate, idR)
                    }
                }
            }, con)
        }else{
            viewModelR?.deleteReservation(numberR)
            val retrofit = getRetrofit().create(APIService::class.java)
            val call = retrofit.updateReservation(numberR, state)
            call?.enqueue(object : Callback<Reservations?> {
                override fun onResponse(
                    call: Call<Reservations?>,
                    response: Response<Reservations?>
                ) {}
                override fun onFailure(call: Call<Reservations?>, t: Throwable) {
                    call.cancel()
                }
            })
        }
    }

    fun cancelReservation(numberR : Int, date : String, idR : Int, clientsN : Int, con : Context){
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateT = df.format(c)

        if(date == dateT){
            getRestaurantsTable(object : CallBack7 {
                override fun onSuccess(result: List<Restaurants>) {
                    val restaurant = result.find { it.idU ==  idR}
                    val numberT = restaurant?.numberT
                    val calculate = numberT?.plus(clientsN)

                    if (calculate != null) {
                        updateRestaurantTableDay(object : CallBack5 {
                            override fun onSuccess() {
                                viewModelR?.deleteReservation(numberR)
                                val retrofit = getRetrofit().create(APIService::class.java)
                                val call = retrofit.deleteReservation(numberR)
                                call?.enqueue(object : Callback<Reservations?> {
                                    override fun onResponse(
                                        call: Call<Reservations?>,
                                        response: Response<Reservations?>
                                    ) {}
                                    override fun onFailure(call: Call<Reservations?>, t: Throwable) {
                                        call.cancel()
                                    }
                                })
                            }
                        }, calculate, idR)
                    }
                }
            }, con)
        }else{
            viewModelR?.deleteReservation(numberR)
            val retrofit = getRetrofit().create(APIService::class.java)
            val call = retrofit.deleteReservation(numberR)
            call?.enqueue(object : Callback<Reservations?> {
                override fun onResponse(
                    call: Call<Reservations?>,
                    response: Response<Reservations?>
                ) {}
                override fun onFailure(call: Call<Reservations?>, t: Throwable) {
                    call.cancel()
                }
            })
        }
    }

    fun updateRestaurantTableDay(callback: CallBack5, number : Int, idR : Int){
        val retrofit = getRetrofit().create(APIService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val call2 = retrofit.updateRestaurantTable(number, idR)
            call2?.enqueue(object : Callback<Users?> {
                override fun onResponse(
                    call: Call<Users?>,
                    response: Response<Users?>
                ) {}
                override fun onFailure(call: Call<Users?>, t: Throwable) { call.cancel() }
            })
            callback.onSuccess()
        }
    }

    fun updateRestaurantTable(callback: CallBack5, number : Int, idR : Int){
        val retrofit = getRetrofit().create(APIService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val call = retrofit.getDataFromRestaurants(query4)
            call.body()?.forEach { restaurant ->
                if(restaurant.idU == idR){
                    val call2 = retrofit.updateRestaurantTable(restaurant.numberT-number, idR)
                    call2?.enqueue(object : Callback<Users?> {
                        override fun onResponse(
                            call: Call<Users?>,
                            response: Response<Users?>
                        ) {}
                        override fun onFailure(call: Call<Users?>, t: Throwable) {
                            call.cancel()
                        }
                    })
                    callback.onSuccess()
                }
            }
        }
    }

    fun insertReservations(date : String, name : String, hour : Int, minute : Int, clientsN : Int, state : String, idC : Int, idR : Int, number : Int, con: Context){
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateT = df.format(c)

        if(date == dateT){
            getRestaurantsTable(object : CallBack7 {
                override fun onSuccess(result: List<Restaurants>) {
                    val restaurant = result.find { it.idU ==  idR}
                    val numberT = restaurant?.numberT
                    if (numberT != null) {
                        if(numberT >= clientsN){
                            updateRestaurantTable(object : CallBack5 {
                                override fun onSuccess() {
                                    getRestaurantReservation(object : CallBack4 {
                                        override fun onSuccess(result: List<Reservations>) {
                                            val reserve = ReservationEntity(result.size+1, date, name, hour, minute, clientsN, state, idC, idR)

                                            viewModelR?.insertReservation(reserve)
                                            createReservation(result.size+1, date, name, hour, minute, clientsN, state, idC, idR)
                                        }
                                    }, con)
                                }
                            }, number, idR)
                        }else{
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(con, "Sorry, no tables available, please try another day", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }, con)
        }else{
            getRestaurantsTable(object : CallBack7 {
                override fun onSuccess(result: List<Restaurants>) {
                    val restaurant = result.find { it.idU == idR }
                    var numberTS = restaurant!!.numberTS
                    getRestaurantReservation(object : CallBack4 {
                        override fun onSuccess(result: List<Reservations>) {
                            result.forEach { reservation ->
                                if(reservation.idR == idR && (reservation.state != "false" && reservation.state != "true") && date == reservation.date){
                                    numberTS -= reservation.clientsN
                                }
                            }
                            if(numberTS >= clientsN){
                                getRestaurantReservation(object : CallBack4 {
                                    override fun onSuccess(result: List<Reservations>) {
                                        val reserve = ReservationEntity(result.size+1, date, name, hour, minute, clientsN, state, idC, idR)

                                        viewModelR?.insertReservation(reserve)
                                        createReservation(result.size+1, date, name, hour, minute, clientsN, state, idC, idR)
                                    }
                                }, con)
                            }else{
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(con, "Sorry, no tables available, please try another day", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }, con)
                }
            }, con)
        }
    }

    fun getRestaurantLocation(callback : CallBack3) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getRestaurantLocation(query6)
            val re = call.body()
            if (re != null) {
                callback.onSuccess(re)
            }
        }
    }
    fun getRestaurantReservation(callback : CallBack4, con : Context){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val call = getRetrofit().create(APIService::class.java).getDataFromReservations(query2)
                val re = call.body()
                if (re != null) {
                    callback.onSuccess(re)
                }
            }catch (e : UnknownHostException) {
                e.printStackTrace()
                printInternetError(con)
            }catch(ioe: IOException){
                ioe.printStackTrace()
                printInternetError(con)
            }catch (he: HttpException){
                he.printStackTrace()
                printInternetError(con)
            }
        }
    }

    fun createReservation(numberR: Int, date: String, name : String, hour: Int, minute: Int, clientsN:Int, state: String, idU: Int, idR: Int){
        val retrofit = getRetrofit().create(APIService::class.java)
        val call = retrofit.createReservation(numberR, date, name, hour, minute, clientsN, state, idU, idR)
        println(date)
        call?.enqueue(object : Callback<Reservations?> {
            override fun onResponse(
                call: Call<Reservations?>,
                response: Response<Reservations?>
            ) {
                val loginResponse: Reservations? = response.body()
                Log.e("keshav", "loginResponse 1 --> $loginResponse")
                if (loginResponse != null) {
                    Log.e("keshav", "getUserId          -->  " + loginResponse.numberR)
                    Log.e("keshav", "getFirstName       -->  " + loginResponse.hour)
                    Log.e("keshav", "getLastName        -->  " + loginResponse.minute)
                }
            }

            override fun onFailure(call: Call<Reservations?>, t: Throwable) {
                call.cancel()
            }
        })
    }

    fun searchUsers(con: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(APIService::class.java).getDataFromUsers(query)
                    if(call.body() != null){
                        call.body()?.forEach {
                            val user = ClientEntity(it.idU, it.username ,it.password, it.email, it.username, it.lastname, it.phone)
                            viewModelU?.insertUser(user)
                        }
                    }
            }catch (e : UnknownHostException) {
                e.printStackTrace()
                printInternetError(con)
            }catch(ioe: IOException){
                ioe.printStackTrace()
                printInternetError(con)
            }catch (he: HttpException){
                he.printStackTrace()
                printInternetError(con)
            }
        }
    }

    fun createUser(user : Users){
        val retrofit = getRetrofit().create(APIService::class.java)
        val call = retrofit.createUser(user.idU, user.username, user.password, user.TypeU, user.image, user.geopos, user.phone, user.price, user.description, user.email, user.name, user.lastname, user.idL)
        println(user)
        call?.enqueue(object : Callback<Users?> {
            override fun onResponse(
                call: Call<Users?>,
                response: Response<Users?>
            ) {
                val loginResponse: Users? = response.body()
                Log.e("keshav", "loginResponse 1 --> $loginResponse")
                if (loginResponse != null) {
                    Log.e("keshav", "getUserId          -->  " + loginResponse.idU)
                    Log.e("keshav", "getFirstName       -->  " + loginResponse.password)
                    Log.e("keshav", "getLastName        -->  " + loginResponse.TypeU)
                }
            }

            override fun onFailure(call: Call<Users?>, t: Throwable) {
                call.cancel()
            }
        })
    }


    /**
     * CLEAR LIST
     */


    companion object {
        var usersList = mutableListOf<Users>()
        var restaurants =  mutableListOf<Restaurants>()
        var restaurantsList = mutableListOf<Users>()
        var reservationsList = mutableListOf<Reservations>()
        var reviewsList = mutableListOf<Reviews>()
        var discountList = mutableListOf<Discounts>()
        var location = mutableListOf<Location>()

        val query = "api.php"
        val query2 = "getReservations.php"
        val query3 = "getReviews.php"
        val query4 = "getRestaurants.php"
        val query5 = "getDiscounts.php"
        val query6 = "getLocation.php"

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://192.168.1.132")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        /**
         * OLD CALLS
         */

        fun searchRestaurants(){
            CoroutineScope(Dispatchers.IO).launch {
                restaurantsList.clear()
                val call = getRetrofit().create(APIService::class.java).getDataFromUsers(query4)
                call.body()?.forEach {
                    restaurantsList.add(it)
                }
            }
        }

        fun searchReviews(){
            CoroutineScope(Dispatchers.IO).launch {
                reviewsList.clear()
                val call = getRetrofit().create(APIService::class.java).getDataFromReviews(query3)
                call.body()?.forEach {
                    reviewsList.add(it)
                }
            }
        }

        fun searchDiscounts(){
            CoroutineScope(Dispatchers.IO).launch {
                discountList.clear()
                val call = getRetrofit().create(APIService::class.java).getDataFromDiscounts(query5)
                call.body()?.forEach {
                    discountList.add(it)
                }
            }
        }

        fun createDiscount(idD: Int, porcentage: Double, idR: Int){
            val retrofit = getRetrofit().create(APIService::class.java)
            val call = retrofit.createDiscount(idD, porcentage, idR)
            call?.enqueue(object : Callback<Discounts?> {
                override fun onResponse(
                    call: Call<Discounts?>,
                    response: Response<Discounts?>
                ) {
                    val loginResponse: Discounts? = response.body()
                    Log.e("keshav", "loginResponse 1 --> $loginResponse")
                    if (loginResponse != null) {
                        Log.e("keshav", "getUserId          -->  " + loginResponse.idD)
                        Log.e("keshav", "getFirstName       -->  " + loginResponse.porcentage)
                        Log.e("keshav", "getLastName        -->  " + loginResponse.idR)
                    }
                }

                override fun onFailure(call: Call<Discounts?>, t: Throwable) {
                    call.cancel()
                }
            })
        }

        fun deleteReservation(numberR : Int){
            val retrofit = getRetrofit().create(APIService::class.java)
            val call = retrofit.deleteReservation(numberR)
            call?.enqueue(object : Callback<Reservations?> {
                override fun onResponse(
                    call: Call<Reservations?>,
                    response: Response<Reservations?>
                ) {
                    val loginResponse: Reservations? = response.body()
                    Log.e("keshav", "loginResponse 1 --> $loginResponse")
                    if (loginResponse != null) {
                        Log.e("keshav", "getUserId          -->  " + loginResponse.numberR)
                        Log.e("keshav", "getFirstName       -->  " + loginResponse.hour)
                        Log.e("keshav", "getLastName        -->  " + loginResponse.minute)
                    }
                }

                override fun onFailure(call: Call<Reservations?>, t: Throwable) {
                    call.cancel()
                }
            })
        }

        fun updateReview(comentary : String, idReview : Int){
            val retrofit = getRetrofit().create(APIService::class.java)
            val call = retrofit.updateReview(comentary, idReview)
            call?.enqueue(object : Callback<Reviews?> {
                override fun onResponse(
                    call: Call<Reviews?>,
                    response: Response<Reviews?>
                ) {
                    val loginResponse: Reviews? = response.body()
                    Log.e("keshav", "loginResponse 1 --> $loginResponse")
                    if (loginResponse != null) {
                        Log.e("keshav", "getUserId          -->  " + loginResponse.idReview)
                        Log.e("keshav", "getFirstName       -->  " + loginResponse.comentary)
                        Log.e("keshav", "getLastName        -->  " + loginResponse.punctuation)
                    }
                }

                override fun onFailure(call: Call<Reviews?>, t: Throwable) {
                    call.cancel()
                }
            })
        }

        fun updateDiscount(porcentage : Double, idD : Int) {
            val retrofit = getRetrofit().create(APIService::class.java)
            val call = retrofit.updateDiscount(porcentage, idD)
            call?.enqueue(object : Callback<Discounts?> {
                override fun onResponse(
                    call: Call<Discounts?>,
                    response: Response<Discounts?>
                ) {
                    val loginResponse: Discounts? = response.body()
                    Log.e("keshav", "loginResponse 1 --> $loginResponse")
                    if (loginResponse != null) {
                        Log.e("keshav", "getUserId          -->  " + loginResponse.idD)
                        Log.e("keshav", "getFirstName       -->  " + loginResponse.porcentage)
                        Log.e("keshav", "getLastName        -->  " + loginResponse.idR)
                    }
                }

                override fun onFailure(call: Call<Discounts?>, t: Throwable) {
                    call.cancel()
                }
            })
        }
    }

    private fun printInternetError(context : Context){
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, "Network error. Maybe your device doesnt have internet",Toast.LENGTH_SHORT).show()
        }
    }
}