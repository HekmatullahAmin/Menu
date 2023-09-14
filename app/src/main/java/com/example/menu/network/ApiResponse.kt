package com.example.menu.network

import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "total") val total: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "results") val results: List<Item>
)
