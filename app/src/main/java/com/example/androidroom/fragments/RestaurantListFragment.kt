package com.example.androidroom.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.androidroom.R
import com.example.androidroom.entities.RestaurantEntity
import com.example.androidroom.repository.ReservationRepository
import com.example.androidroom.viewModel.MainMenuRestaurantViewModel
import com.example.androidroom.viewModel.ReservationViewModel
import com.example.androidroom.viewModel.RestaurantViewModel
import com.example.androidroom.webService.Retrofit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantListFragment(viewM: MainMenuRestaurantViewModel, viewR : ReservationViewModel): Fragment() {

    private val viewModel = viewM
    private val viewModel2 = viewR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retro = Retrofit(viewModel, viewModel2, null, null)
        context?.let { retro.getRestaurants(it) }

        val searchView = requireView().findViewById<SearchView>(R.id.search)
        val localList = mutableListOf<RestaurantEntity>()
        var local = false

        if(!local){
            viewModel.allRestaurant.observe(viewLifecycleOwner) { restaurant ->
                if(restaurant.size > 2){
                    replaceFragment(RestaurantList(restaurant, viewModel, viewModel2))
                    local = true
                }else{
                    replaceFragment(RestaurantList(restaurant, viewModel, viewModel2))
                }
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.allRestaurant.observe(viewLifecycleOwner) { restaurant ->
                    if(restaurant.size > 2){
                        localList.clear()
                        restaurant.forEach {
                            if(it.nameR.contains(p0.toString())){
                                localList.add(it)
                            }
                        }
                        replaceFragment(RestaurantList(localList, viewModel, viewModel2))
                    }else{
                        replaceFragment(RestaurantList(restaurant, viewModel, viewModel2))
                    }
                }
                return false
            }
        })
    }

    private fun replaceFragment(fragment: RestaurantList) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment2, fragment)
        fragmentTransaction.commit()
    }

    /*private fun replaceFragment2(fragment: RestaurantListHorizontal) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.topList, fragment)
        fragmentTransaction.commit()
    }*/
}