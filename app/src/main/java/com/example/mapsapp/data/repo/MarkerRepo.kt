package com.example.mapsapp.data.repo

import com.example.mapsapp.domain.model.MarkerEntity

interface MarkerRepo {

    suspend fun getAllMarkers(): List<MarkerEntity>

    suspend fun insertMarker(marker: MarkerEntity)

    suspend fun deleteMarker(marker: MarkerEntity)

    suspend fun updateMarker(marker: MarkerEntity)

    suspend fun updateAllMarkers(list: List<MarkerEntity>)
}