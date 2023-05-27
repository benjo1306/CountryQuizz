package com.example.countryquizz.di

import com.example.countryquizz.data.firebase.CloudStorage
import com.example.countryquizz.data.firebase.FirebaseService
import com.example.countryquizz.data.repos.CountryQuizzRepository
import com.example.countryquizz.ui.viewmodels.PlayOrSeeResultsViewModel
import com.example.countryquizz.ui.viewmodels.PlayQuizViewModel
import com.example.countryquizz.ui.viewmodels.RegistrationViewModel
import com.example.countryquizz.ui.viewmodels.SeeResultsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RegistrationViewModel(FirebaseService(FirebaseAuth.getInstance())) }
    viewModel { PlayOrSeeResultsViewModel(CloudStorage(FirebaseStorage.getInstance(), FirebaseAuth.getInstance())) }
    viewModel { PlayQuizViewModel(CountryQuizzRepository()) }
    viewModel { SeeResultsViewModel(FirebaseFirestore.getInstance(), CloudStorage(FirebaseStorage.getInstance(), FirebaseAuth.getInstance()), FirebaseAuth.getInstance()) }
}