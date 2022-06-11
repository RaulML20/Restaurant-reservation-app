package com.example.androidroom.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.androidroom.CallBack4
import com.example.androidroom.R
import com.example.androidroom.data.Reservations
import com.example.androidroom.entities.ReservationEntity
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.webService.Retrofit

class ReservationRestaurants(viewR : ReservationViewModel) : Fragment() {

    private val sharedPreferencesManager = SharedPreferencesManager()
    val viewModel = viewR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
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
                    .height(700.dp)
                    .padding(0.dp, 30.dp, 0.dp, 60.dp)
            ) {
                items(list) { items ->
                    showReservationList(items, retro)
                }
            }
        }
    }

    @Composable
    fun showReservationList(items: ReservationEntity, retro : Retrofit){
        Card(elevation = 4.dp, modifier = Modifier
            .padding(18.dp, 10.dp, 18.dp, 8.dp).height(110.dp).width(350.dp)
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
                            modifier = Modifier.size(85.dp).padding(50.dp, 0.dp, 0.dp, 25.dp).clickable {
                                acceptReservationDialog(retro, items.numberR)
                            },
                            alignment = Alignment.CenterEnd
                        )

                        Image(
                            painterResource(id = R.drawable.cancel),
                            contentDescription = "Cancel",
                            modifier = Modifier.size(210.dp).padding(180.dp, 15.dp, 0.dp, 25.dp).clickable {
                                cancelReservationDialog(retro, items.numberR)
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

    private fun acceptReservationDialog(retro : Retrofit, numberR : Int){
        val alertDialog : AlertDialog = this.let{
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.state)
            builder.apply {
                setPositiveButton(R.string.cancel
                ) { dialog, id ->
                    retro.updateReservation(numberR, true)
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

    private fun cancelReservationDialog(retro : Retrofit, numberR : Int){
        val alertDialog : AlertDialog = this.let{
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.state)
            builder.apply {
                setPositiveButton(R.string.cancel
                ) { dialog, id ->
                    retro.updateReservation(numberR, false)
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