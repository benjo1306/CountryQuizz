package com.example.countryquizz.data.api

import com.example.countryquizz.model.RestCountriesResponse
import retrofit2.http.GET

interface RestCountries {

    @GET("all")
    suspend fun getAllCountries() : ArrayList<RestCountriesResponse>
}