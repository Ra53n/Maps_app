package com.example.mapsapp.presentation.di

import com.example.mapsapp.presentation.viewModel.MainViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single {
        LocationServices.getFusedLocationProviderClient(
            androidContext()
        )
    }
    viewModel { MainViewModel(get()) }
}