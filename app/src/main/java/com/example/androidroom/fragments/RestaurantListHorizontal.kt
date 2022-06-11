package com.example.androidroom.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidroom.R

data class items(val text : String, val icon : Int)

class RestaurantListHorizontal : Fragment(){
    val listItems = mutableListOf<items>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val item = items("Best restaurants", R.drawable.best_r)
        val item2 = items("Sushi", R.drawable.sushi)
        val item3 = items("Mexican", R.drawable.taco)
        val item4 = items("American", R.drawable.american)
        val item5 = items("China", R.drawable.china)
        listItems.add(item)
        listItems.add(item2)
        listItems.add(item3)
        listItems.add(item4)
        listItems.add(item5)

        return ComposeView(requireContext()).apply {
            setContent {
                showHorizontalList(listItems)
            }
        }
    }

    @Composable
    fun showHorizontalList(list: MutableList<items>){
        Row{
            LazyRow(modifier = Modifier
                .height(100.dp)
                .padding(0.dp, 10.dp, 0.dp, 0.dp)){
                items(list) { items ->
                    horizontalList(items)
                }
            }
        }
    }

    @Composable
    fun horizontalList(items : items){
        Column{
            Image(
                painterResource(id = items.icon),
                contentDescription = "icon image",
                modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally)
            )
            Text(
                text = items.text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
            )
        }
    }
}