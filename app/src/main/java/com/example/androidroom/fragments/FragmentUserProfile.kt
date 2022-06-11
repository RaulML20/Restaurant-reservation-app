package com.example.androidroom.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import com.example.androidroom.CallBack4
import com.example.androidroom.CallBack6
import com.example.androidroom.R
import com.example.androidroom.data.Reservations
import com.example.androidroom.data.Reviews
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.view.MainHall
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.viewModel.ReviewViewModel
import com.example.androidroom.webService.Retrofit

class FragmentUserProfile(val viewModel : ReservationViewModel, val viewModelR : ReviewViewModel) : Fragment() {

    private val sharedPreferencesManager = SharedPreferencesManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logout = requireView().findViewById<Button>(R.id.logout)
        val name = requireView().findViewById<TextView>(R.id.name)
        val lastname = requireView().findViewById<TextView>(R.id.lastname)
        val phone = requireView().findViewById<TextView>(R.id.phone)
        val reservations = requireView().findViewById<TextView>(R.id.reservations)
        val reviews = requireView().findViewById<TextView>(R.id.reviews)


        var reservationsCount = 0
        var reviewsCount = 0
        val retro = Retrofit(null, viewModel, null, viewModelR)

        retro.getRestaurantReservation(object : CallBack4 {
            override fun onSuccess(result: List<Reservations>) {
                result.forEach {
                    if(it.idU == sharedPreferencesManager.getIdC(requireContext())){
                        reservationsCount++
                        println(reservationsCount)
                    }
                }
            }
        }, requireContext())

        retro.searchReviews(object : CallBack6 {
            override fun onSuccess(result: List<Reviews>?) {
                result?.forEach {
                    if(it.idU == sharedPreferencesManager.getIdC(requireContext())){
                        reviewsCount++
                        println(reviewsCount)
                    }
                }
            }
        }, requireContext())

        Thread.sleep(400)
        name.text = sharedPreferencesManager.getUsername(requireContext())
        lastname.text = sharedPreferencesManager.getLastname(requireContext())
        phone.text = sharedPreferencesManager.getPhone(requireContext())
        reservations.text = reservationsCount.toString()
        reviews.text = reviewsCount.toString()

        logout.setOnClickListener {
            sharedPreferencesManager.clear(requireContext())
            val intent = Intent(context, MainHall::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}