package com.example.countryquizz.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countryquizz.data.repos.CountryQuizzRepository
import com.example.countryquizz.model.RestCountriesResponse
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlayQuizViewModel(private val repository: CountryQuizzRepository) : ViewModel() {

    var didFetchCountries : MutableLiveData<Boolean> = MutableLiveData()
    private var countriesInfo : ArrayList<RestCountriesResponse> = ArrayList()
    private var correctAnswers: Int = 0
    var allQuestionsAnswered : MutableLiveData<Boolean> = MutableLiveData()
    var currentQuestionFlag : MutableLiveData<Int> = MutableLiveData()
    var isAnswerCorrect: MutableLiveData<Boolean> = MutableLiveData()

    var currentQuestion: Int = 0

    private var currentImageName: String = ""
    var selectedCountries: ArrayList<RestCountriesResponse> = ArrayList()
    var selectedCountryImage: String = ""

    fun getAllCountries() {
        viewModelScope.launch {
            countriesInfo = repository.getAllCountries()
            if(countriesInfo.isNotEmpty())
                didFetchCountries.postValue(true)
        }
    }

    fun getRandomCountryName() : String {
        if(selectedCountries.size == 4)
            selectedCountries.clear()

        val randomNumber = Random.nextInt(countriesInfo.size)
        val randomCountry = countriesInfo[randomNumber]

        countriesInfo.removeAt(randomNumber)

        selectedCountries.add(randomCountry)

        return randomCountry.name.common
    }

    fun getRandomCountryImage(): String {
        val rnd = Random.nextInt(selectedCountries.size)
        selectedCountryImage = selectedCountries[rnd].name.common
        currentImageName = selectedCountries[rnd].flags[1]

        return currentImageName
    }

    fun isCorrect(answer: String) {

        if(answer == selectedCountryImage) {
            correctAnswers++

            currentQuestion++
            currentQuestionFlag.postValue(currentQuestion)

            isAnswerCorrect.postValue(true)
        }
        else {
            currentQuestion++
            currentQuestionFlag.postValue(currentQuestion)

            isAnswerCorrect.postValue(false)
        }

        if(currentQuestion == 5) {
            allQuestionsAnswered.postValue(true);
        }
    }

    fun getCorrectAnswers() : Int {
        return correctAnswers
    }
}