package com.example.countryquizz.data.api

import android.util.Log
import com.example.countryquizz.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RestCountries by lazy {
        retrofit.create(RestCountries::class.java)
    }
}