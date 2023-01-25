package com.example.mapsapp.presentation.viewModel

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng

class MainViewModel constructor(
    private val locationService: FusedLocationProviderClient,
) : ViewModel() {

    private val _myLocationLiveData = MutableLiveData<LatLng>()
    val myLocationLiveData: LiveData<LatLng> by this::_myLocationLiveData

    private val locationRequest = LocationRequest.Builder(DEVICE_LOCATION_REFRESH_PERIOD)
        .setDurationMillis(DEVICE_LOCATION_REQUEST_DURATION)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            super.onLocationResult(location)
            val latitude = location.lastLocation?.latitude ?: 0.0
            val longitude = location.lastLocation?.longitude ?: 0.0
            _myLocationLiveData.value = LatLng(latitude, longitude)
        }
    }

    fun stopLocationUpdates() {
        locationService.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun requestMyLocation() {
        locationService.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    companion object {
        private const val DEVICE_LOCATION_REFRESH_PERIOD = 2000L
        private const val DEVICE_LOCATION_REQUEST_DURATION = 60000L
    }
}