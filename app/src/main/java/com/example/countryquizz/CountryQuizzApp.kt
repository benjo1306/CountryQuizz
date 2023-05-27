package com.example.countryquizz

import android.app.Application
import android.content.Context
import com.example.countryquizz.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CountryQuizzApp : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        startKoin {
            androidContext(this@CountryQuizzApp)
            modules(viewModelModule)
        }
    }
}