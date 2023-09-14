package com.example.menu.network

import com.squareup.moshi.Json

data class Item(
    @Json(name = "alt_description") val description: String,
    val urls: ImageUrl
)

