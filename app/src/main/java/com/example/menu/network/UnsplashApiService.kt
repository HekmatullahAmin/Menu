package com.example.menu.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.unsplash.com/"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface UnsplashApiService {
    @GET("/search/photos")
    suspend fun searchItem(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("client_id") clientId: String
    ): ApiResponse
}

object UnsplashApi {
    val service: UnsplashApiService by lazy {
        retrofit.create(UnsplashApiService::class.java)
    }
}