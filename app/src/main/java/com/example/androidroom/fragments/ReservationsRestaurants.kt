package com.example.androidroom.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.androidroom.Alarm
import com.example.androidroom.CallBack4
import com.example.androidroom.R
import com.example.androidroom.data.Reservations
import com.example.androidroom.entities.ReservationEntity
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.view.MainHall
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.webService.Retrofit


class ReservationRestaurants(viewR : ReservationViewModel) : Fragment() {

    private val alarm : Alarm = Alarm()
    private val sharedPreferencesManager = SharedPreferencesManager()
    val viewModel = viewR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {

            val alarmUp = PendingIntent.getBroadcast(context, 0, Intent(context, Alarm::class.java), PendingIntent.FLAG_NO_CREATE) != null

            if (alarmUp) {
                Log.d("myTag", "Alarm is already active")
            }else{
                alarm.setAlarm(context)
                Log.d("myTag", "Alarm is on now")
            }


            val retro = Retrofit(null, viewModel, null, null)
            retro.getRestaurantReservation(object : CallBack4 {
                override fun onSuccess(result: List<Reservations>) {
                    viewModel.deleteAll()
                    result.forEach{
                        if(sharedPreferencesManager.getIdC(context) == it.idR && it.state == "desactivate"){
                            val reserve = ReservationEntity(it.numberR, it.date, it.name, it.hour, it.minute, it.clientsN, it.state, it.idU, it.idR)
                            viewModel.insertReservation(reserve)
                        }
                    }
                }
            }, context)
            viewModel.allReservations.observe(viewLifecycleOwner){ reservations ->
                setContent {
                    startRestaurantList(reservations, retro)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Composable
    fun startRestaurantList(list : List<ReservationEntity>, retro : Retrofit) {
        Column {
            Card(
                elevation = 4.dp, modifier = Modifier
                    .padding(18.dp, 30.dp, 18.dp, 8.dp)
                    .align(Alignment.CenterHorizontally)
                , shape = RoundedCornerShape(16.dp)
            ) {
                Row {
                    Box(
                        contentAlignment = Alignment.TopCenter, modifier = Modifier
                            .padding(10.dp, 5.dp, 10.dp, 5.dp)
                    ) {
                        Text(
                            text = "RESERVATIONS",
                            fontWeight = FontWeight.Bold,
                            color = Color(142, 38, 202),
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(2.dp, 5.dp, 0.dp, 0.dp)
                        )
                        Image(
                            painterResource(id = R.drawable.reservation),
                            contentDescription = "icon",
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(5.dp, 50.dp, 0.dp, 0.dp)
                                .size(30.dp)
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .height(530.dp)
                    .padding(0.dp, 30.dp, 0.dp, 60.dp)
            ) {
                items(list) { items ->
                    showReservationList(items, retro)
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                FloatingActionButton(onClick = {
                    context?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarm.setAlarmNow(it)
                        }
                    }
                }, modifier = Modifier
                    .padding(20.dp, 0.dp, 20.dp, 0.dp), backgroundColor = Color(0xFFFB8C00)) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = "update tables")
                }
                FloatingActionButton(onClick = {
                    context?.let { alarm.cancelAlarm(it) }
                    context?.let { sharedPreferencesManager.setUsername(it, "") }
                    val intent = Intent(context, MainHall::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }, modifier = Modifier
                    .padding(0.dp, 0.dp, 20.dp, 0.dp), backgroundColor = Color(0xFFFB8C00)) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "close session")
                }
            }
        }
    }

    @Composable
    fun showReservationList(items: ReservationEntity, retro : Retrofit){
        Card(elevation = 4.dp, modifier = Modifier
            .padding(18.dp, 10.dp, 18.dp, 8.dp)
            .height(110.dp)
            .width(350.dp)
            ,shape = RoundedCornerShape(16.dp)
        ){
            Column {
                Row{
                    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier
                        .padding(10.dp, 5.dp, 10.dp, 5.dp)
                        .fillMaxSize()) {
                        Text(
                            text = items.name,
                            fontWeight = FontWeight.Bold,
                            color = Color(167, 91, 180),
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(2.dp, 5.dp, 0.dp, 0.dp)
                        )
                        Text(
                            text = "Date: "+items.date,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(2.dp, 0.dp, 0.dp, 7.dp)
                        )
                        Image(
                            painterResource(id = R.drawable.accept),
                            contentDescription = "Acept",
                            modifier = Modifier
                                .size(85.dp)
                                .padding(50.dp, 0.dp, 0.dp, 25.dp)
                                .clickable {
                                    acceptReservationDialog(retro, items.numberR, items.idCreatorR, items.date, items.clientsN)
                                },
                            alignment = Alignment.CenterEnd
                        )

                        Image(
                            painterResource(id = R.drawable.cancel),
                            contentDescription = "Cancel",
                            modifier = Modifier
                                .size(210.dp)
                                .padding(180.dp, 15.dp, 0.dp, 25.dp)
                                .clickable {
                                    cancelReservationDialog(retro, items.numberR, items.idCreatorR, items.date, items.clientsN)
                                },
                            alignment = Alignment.CenterEnd
                        )
                        Text(
                            text = "Hour: ${items.hour}:${items.minute}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(2.dp, 9.dp, 0.dp, 0.dp)
                        )
                    }
                }
            }
        }
    }

    private fun acceptReservationDialog(retro : Retrofit, numberR : Int, idR : Int, date : String, clientsN : Int){
        val alertDialog : AlertDialog = this.let{
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.state)
            builder.apply {
                setPositiveButton(R.string.cancel
                ) { dialog, id ->
                    retro.updateReservation(numberR, true, date, clientsN, context, idR)
                    dialog.dismiss()
                }
                setNegativeButton(R.string.mod
                ) { dialog, id ->
                    dialog.dismiss()
                }
            }
            builder.create()
            builder.show()
        }
    }

    private fun cancelReservationDialog(retro : Retrofit, numberR : Int, idR : Int, date : String, clientsN : Int){
        val alertDialog : AlertDialog = this.let{
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.state)
            builder.apply {
                setPositiveButton(R.string.cancel
                ) { dialog, id ->
                    retro.updateReservation(numberR, false, date, clientsN, context, idR)
                    dialog.dismiss()
                }
                setNegativeButton(R.string.mod
                ) { dialog, id ->
                    dialog.dismiss()
                }
            }
            builder.create()
            builder.show()
        }
    }
}