package com.example.androidroom.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.androidroom.CallBack6
import com.example.androidroom.R
import com.example.androidroom.data.Reviews
import com.example.androidroom.sharedpref.SharedPreferencesManager
import com.example.androidroom.viewModel.ReviewViewModel
import com.example.androidroom.webService.Retrofit

class FragmentReviewList(viewR : ReviewViewModel) : Fragment(){

    val viewModel = viewR
    private val sharedPreferencesManager = SharedPreferencesManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {

            val retro = Retrofit(null, null, null, viewModel)
            val reviews = mutableListOf<Reviews>()
            retro.searchReviews(object : CallBack6 {
                override fun onSuccess(result: List<Reviews>?) {
                    result?.forEach {
                        if(it.idR == sharedPreferencesManager.getIdC(context)) {
                            reviews.add(it)
                        }
                    }
                    run{
                        setContent {
                            if (result != null) {
                                showReviews(reviews)
                            }
                        }
                    }
                }
            }, context)
        }
    }
}

@Composable
fun showReviewBody(item : Reviews){
    Row(modifier = Modifier.padding(10.dp, 20.dp, 0.dp, 0.dp)){
        Image(painter = painterResource(R.drawable.profile),
            contentDescription = "User icon",
            modifier = Modifier.size(38.dp).padding(0.dp, 10.dp, 0.dp, 0.dp)
        )

        Spacer(modifier = Modifier.width(15.dp))
        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }.padding(0.dp, 0.dp, 0.dp, 0.dp)){

            Row{
                Text(item.email.toString(), modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp))

                when(item.punctuation){
                    1 -> Image(painterResource(R.drawable.star1), contentDescription = "Punctuation", modifier = Modifier.size(60.dp).width(60.dp).height(20.dp).padding(10.dp, 0.dp, 0.dp, 0.dp))
                    2 -> Image(painterResource(R.drawable.star2), contentDescription = "Punctuation", modifier = Modifier.width(60.dp).height(20.dp).padding(10.dp, 0.dp, 0.dp, 0.dp))
                    3 -> Image(painterResource(R.drawable.star3), contentDescription = "Punctuation", modifier = Modifier.width(60.dp).height(20.dp).padding(10.dp, 0.dp, 0.dp, 0.dp))
                    4 -> Image(painterResource(R.drawable.star4), contentDescription = "Punctuation", modifier = Modifier.width(60.dp).height(20.dp).padding(10.dp, 0.dp, 0.dp, 0.dp))
                    5 -> Image(painterResource(R.drawable.star5), contentDescription = "Punctuation", modifier = Modifier.width(60.dp).height(20.dp).padding(10.dp, 0.dp, 0.dp, 0.dp))
                }
            }

            Surface(
                shape = MaterialTheme.shapes.medium, elevation = 1.dp, modifier = Modifier.width(300.dp).padding(0.dp, 10.dp, 0.dp, 0.dp)
            ){
                Text(text = item.comentary, modifier = Modifier.padding(all = 10.dp), maxLines = if (isExpanded) Int.MAX_VALUE else 1, style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
fun showReviews(list : List<Reviews>){
    LazyColumn {
        items(list){ items ->
            showReviewBody(items)
        }
    }
}
