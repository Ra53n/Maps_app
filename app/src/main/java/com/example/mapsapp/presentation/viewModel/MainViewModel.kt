package com.example.mapsapp.presentation.viewModel

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.data.repo.MarkerRepo
import com.example.mapsapp.domain.model.MarkerEntity
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel constructor(
    private val locationService: FusedLocationProviderClient,
    private val repo: MarkerRepo
) : ViewModel() {

    private val _myLocationLiveData = MutableLiveData<LatLng>()
    val myLocationLiveData: LiveData<LatLng> by this::_myLocationLiveData

    private val _markersLiveData = MutableLiveData<List<MarkerEntity>>()
    val markersLiveData: LiveData<List<MarkerEntity>> by this::_markersLiveData

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

    fun onAddMarker(latLng: LatLng) {
        viewModelScope.launch {
            repo.insertMarker(
                MarkerEntity(
                    id = Random().nextInt(),
                    latLng = latLng,
                    title = "",
                    annotation = ""
                )
            )
        }
    }

    fun getMarkers() {
        viewModelScope.launch {
            _markersLiveData.postValue(repo.getAllMarkers())
        }
    }


    companion object {
        private const val DEVICE_LOCATION_REFRESH_PERIOD = 2000L
        private const val DEVICE_LOCATION_REQUEST_DURATION = 60000L
    }
}