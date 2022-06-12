package com.example.androidroom.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.androidroom.CallBack7
import com.example.androidroom.R
import com.example.androidroom.data.Restaurants
import com.example.androidroom.databinding.ActivityReservationBinding
import com.example.androidroom.fragments.DatePicker
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.viewModel.MainMenuRestaurantViewModel
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.webService.Retrofit
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class Reservation : AppCompatActivity(), AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    private val viewModel: ReservationViewModel by viewModels()
    private val viewModel2: MainMenuRestaurantViewModel by viewModels()

    private lateinit var binding: ActivityReservationBinding
    private val sharedPreferencesManager = SharedPreferencesManager()
    var hour = 16
    var minute = 0
    private var date : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)

        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        date = df.format(c)

        setContentView(binding.root)
        binding.hours.onItemSelectedListener = this
        val dateD: String = SimpleDateFormat("dd/MM/yyyy").format(Date())
        val editableD: Editable = SpannableStringBuilder(dateD)
        binding.date.text = editableD

        ArrayAdapter.createFromResource(
            this,
            R.array.hours,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.hours.adapter = adapter
        }

        binding.date.setOnClickListener {
            val datePicker: DialogFragment = DatePicker()
            datePicker.show(supportFragmentManager, "selector fecha")
        }

        binding.book.setOnClickListener {
            val idC = sharedPreferencesManager.getIdC(this)
            val idR = intent.getIntExtra("idR", 0)

            val result = validation()

            if(result){
                val retro = Retrofit(viewModel2 , viewModel, null, null)
                retro.insertReservations(date, binding.first.text.toString(), hour, minute, binding.nclients.text.toString().toInt(), "desactivate", idC, idR, binding.nclients.text.toString().toInt(), this@Reservation)
                finish()
            }else{
                Toast.makeText(this, "fields must not be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d("mensaje",p2.toString())
        when (p2) {
            0 -> {
                hour = 16
                minute = 0
            }
            1 -> {
                hour = 16
                minute = 30
            }
            2 -> {
                hour = 17
                minute = 0
            }
            3 -> {
                hour = 17
                minute = 30
            }
            4 -> {
                hour = 18
                minute = 0
            }
            5 -> {
                hour = 18
                minute = 30
            }
            6 -> {
                hour = 19
                minute = 0
            }
            7 -> {
                hour = 19
                minute = 30
            }
            8 -> {
                hour = 20
                minute = 0
            }
            9 -> {
                hour = 20
                minute = 30
            }
            10 -> {
                hour = 21
                minute = 0
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        hour = 16
        minute = 0
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date= "${dayOfMonth}/${month+1}/${year}"
        val editable: Editable = SpannableStringBuilder(date)
        binding.date.text = editable
    }

    fun validation() : Boolean{
        if(binding.nclients.text.isNotEmpty() && binding.first.text.isNotEmpty()){
            return true
        }
        return false
    }
}