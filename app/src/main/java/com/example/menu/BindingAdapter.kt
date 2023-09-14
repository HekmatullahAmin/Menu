package com.example.menu

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.menu.network.UnsplashApiStatus

@BindingAdapter("bindStatus")
fun bindStatus(image: ImageView, status: UnsplashApiStatus?) {
    when (status) {
        UnsplashApiStatus.ERROR -> {
            image.visibility = View.VISIBLE
            image.setImageResource(R.drawable.ic_connection_error)
        }

        UnsplashApiStatus.LOADING -> {
            image.visibility = View.VISIBLE
            image.setImageResource(R.drawable.loading_animation)
        }

        UnsplashApiStatus.SUCCESS -> {
            image.visibility = View.GONE
        }

        else -> {
        }
    }
}