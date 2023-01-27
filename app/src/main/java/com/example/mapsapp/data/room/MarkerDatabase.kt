package com.example.mapsapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MarkerModel::class], version = 1, exportSchema = false)
abstract class MarkerDatabase : RoomDatabase() {
    abstract fun markerDao(): MarkerDao
}