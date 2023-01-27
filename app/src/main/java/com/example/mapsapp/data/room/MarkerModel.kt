package com.example.mapsapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarkerModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val lat: Double,
    val lng: Double,
    val title: String,
    val annotation: String
)

