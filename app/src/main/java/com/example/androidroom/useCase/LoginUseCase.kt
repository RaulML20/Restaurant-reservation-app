package com.example.androidroom.useCase
import android.content.Context
import com.example.androidroom.AESEncyption
import com.example.androidroom.entities.ClientEntity
import com.example.androidroom.entities.RestaurantEntity
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.viewModel.LoginViewModel
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val login : LoginViewModel){

    private val sharedPreferencesManager = SharedPreferencesManager()
    fun loginComprobation(context : Context, username: String, password: String, users: List<ClientEntity>?, restaurants: List<RestaurantEntity>?) : Boolean {
        users?.forEach { client ->
            val decryptpassword = AESEncyption.decrypt(client.password)

            if(client.email == username && decryptpassword == password){
                sharedPreferencesManager.setType(context, "client")
                sharedPreferencesManager.setUsername(context, client.username)
                sharedPreferencesManager.setLastname(context, client.lastname)
                sharedPreferencesManager.setEmail(context, client.email)
                sharedPreferencesManager.setPhone(context, client.phone)
                return true
            }
        }

        restaurants?.forEach { restaurant ->
            val decryptpassword = AESEncyption.decrypt(restaurant.password)

            if(restaurant.nameR == username && decryptpassword == password){
                sharedPreferencesManager.setType(context, "restaurant")
                return true
            }
        }
        return false
    }

    fun getUserId(username : String, list : List<ClientEntity>) : Int{
        list.forEach {
            if(it.email == username){
                return it.idC
            }
        }
        return 0
    }

    fun getRestaurantId(username : String, list : List<RestaurantEntity>) : Int{
        list.forEach {
            if(it.nameR == username){
                return it.idR
            }
        }
        return 0
    }
}