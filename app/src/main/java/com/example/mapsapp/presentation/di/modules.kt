package com.example.mapsapp.presentation.di

import androidx.room.Room
import com.example.mapsapp.data.mapper.MarkerModelToEntityMapper
import com.example.mapsapp.data.repo.MarkerRepo
import com.example.mapsapp.data.repo.MarkerRepoImpl
import com.example.mapsapp.data.room.MarkerDatabase
import com.example.mapsapp.presentation.viewModel.MainViewModel
import com.example.mapsapp.presentation.viewModel.MarkersEditingViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    single {
        Room.databaseBuilder(androidContext(), MarkerDatabase::class.java, "markers.db")
            .build()
    }
    single { MarkerModelToEntityMapper() }
    single<MarkerRepo> { MarkerRepoImpl(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { MarkersEditingViewModel(get()) }
}