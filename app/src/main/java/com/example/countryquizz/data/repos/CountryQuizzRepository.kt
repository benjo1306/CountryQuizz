package com.example.countryquizz.data.repos

import com.example.countryquizz.data.api.RetrofitInstance
import com.example.countryquizz.model.RestCountriesResponse

class CountryQuizzRepository {

    suspend fun getAllCountries() : ArrayList<RestCountriesResponse> {
        try {
            return RetrofitInstance.api.getAllCountries()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return arrayListOf()
    }
}