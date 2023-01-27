package com.example.mapsapp.domain.model

import com.google.android.gms.maps.model.LatLng

data class MarkerEntity(
    val id: Int,
    val latLng: LatLng,
    val title: String,
    val annotation: String
)