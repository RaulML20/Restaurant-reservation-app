package com.example.androidroom.sharedpref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesManager {
    private val APP_SETTINGS = "APP_SETTINGS"

    // properties
    private val username = "SOME_STRING_VALUE"
    private val idC = "idC"
    private val TypeU = "Client"
    private val lastname = "lastname"
    private val email = "email"
    private val phone = "phone"
    private val longitude = "longitude"
    private val latitude = "latitude"
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getUsername(context: Context): String? {
        return getSharedPreferences(context).getString(username, null)
    }
    @SuppressLint("CommitPrefEdits")
    fun clear(context : Context){
        val editor = getSharedPreferences(context).edit()
        editor.clear().apply()
    }

    fun setUsername(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(username, newValue)
        editor.apply()
    }

    fun setLongitude(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(longitude, newValue)
        editor.apply()
    }

    fun setLatitude(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(latitude, newValue)
        editor.apply()
    }

    fun setPhone(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(phone, newValue)
        editor.apply()
    }

    fun setEmail(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(email, newValue)
        editor.apply()
    }

    fun setLastname(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(lastname, newValue)
        editor.apply()
    }

    fun getLatitude(context: Context): String? {
        return getSharedPreferences(context).getString(latitude, "")
    }

    fun getLongitude(context: Context): String? {
        return getSharedPreferences(context).getString(longitude, "")
    }

    fun getLastname(context: Context): String? {
        return getSharedPreferences(context).getString(lastname, "")
    }

    fun getPhone(context: Context): String? {
        return getSharedPreferences(context).getString(phone, "")
    }

    fun getEmail(context: Context): String? {
        return getSharedPreferences(context).getString(email, "")
    }

    fun getIdC(context: Context): Int {
        return getSharedPreferences(context).getInt(idC, 0)
    }

    fun setIdc(context: Context, newValue: Int) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(idC, newValue)
        editor.apply()
    }

    fun getType(context: Context): String? {
        return getSharedPreferences(context).getString(TypeU, null)
    }

    fun setType(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(TypeU, newValue)
        editor.apply()
    }
}