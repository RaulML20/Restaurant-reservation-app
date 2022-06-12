package com.example.androidroom

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.androidroom.view.MenuRestaurants
import kotlin.math.floor


class Notification {
    @RequiresApi(Build.VERSION_CODES.O)
    fun create(con: Context) {
        createNotificationChannel(con)
        val intent = Intent(con, MenuRestaurants::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(con, 0, intent, 0)
        val builder = Notification.Builder(con, "ID")
            .setSmallIcon(R.drawable.btn_star)
            .setContentTitle("Book-It")
            .setContentText("¡Restaurant tables updated!")
            .setContentIntent(pendingIntent)
        val notification = NotificationManagerCompat.from(con)

        val randomID = floor(Math.random() * (9999 - 1000 + 1) + 1000).toInt()
        notification.notify(randomID, builder.build())
    }

    private fun createNotificationChannel(con: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "CANAL1"
            val CHANNEL_ID = "ID"
            val description = "Notificación por no fichar"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager: NotificationManager = con.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}
