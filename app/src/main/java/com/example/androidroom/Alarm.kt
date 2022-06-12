package com.example.androidroom

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.androidroom.data.Reservations
import com.example.androidroom.data.Restaurants
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.webService.Retrofit
import java.text.SimpleDateFormat
import java.util.*


class Alarm : BroadcastReceiver() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        @SuppressLint("InvalidWakeLockTag") val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "")
        wl.acquire(10*60*1000L /*10 minutes*/)

        updateRestaurantTables(object : CallBack8 {
            override fun onSuccess(result : Int) {
                println(result)
                val sharedPreferencesManager = SharedPreferencesManager()
                val idR = sharedPreferencesManager.getIdC(context)
                val retrofit = Retrofit(null, null, null, null)

                retrofit.updateRestaurantTableDay(object : CallBack5 {
                    override fun onSuccess() {
                        val notification = Notification()
                        notification.create(context)
                    }
                }, result, idR)
            }
        }, context)

        wl.release()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarm(context: Context) {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()

        cal.set(Calendar.HOUR_OF_DAY, 1)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        if (cal.get(Calendar.HOUR_OF_DAY) >= 1) {
            Log.e("TAG", "Alarm will schedule for next day!");
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        else{
            Log.e("TAG", "Alarm will schedule for today!");
        }

        println("Se inicio")
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, Alarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, INTERVAL_DAY, pi)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setAlarmNow(context: Context){
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()

        cal.set(Calendar.HOUR_OF_DAY, 12)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, Alarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, Alarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    private fun updateRestaurantTables(callBack: CallBack8, context : Context){
        val retrofit = Retrofit(null, null, null, null)
        val sharedPreferencesManager = SharedPreferencesManager()
        val idR = sharedPreferencesManager.getIdC(context)

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = df.format(c)
        println(date)

        retrofit.getRestaurantsTable(object : CallBack7 {
            override fun onSuccess(result: List<Restaurants>) {
                val restaurant = result.find { it.idU == idR }
                var numberTS = restaurant!!.numberTS
                retrofit.getRestaurantReservation(object : CallBack4 {
                    override fun onSuccess(result: List<Reservations>) {
                        result.forEach { reservation ->
                            if(reservation.idR == idR && (reservation.state != "false" && reservation.state != "true") && reservation.date == date){
                                numberTS -= reservation.clientsN
                            }
                        }
                        println(result)
                        callBack.onSuccess(numberTS)
                    }
                }, context)
            }
        }, context)
    }
}