package com.example.mapsapp.data.repo

import com.example.mapsapp.data.mapper.MarkerModelToEntityMapper
import com.example.mapsapp.data.room.MarkerDatabase
import com.example.mapsapp.domain.model.MarkerEntity

class MarkerRepoImpl(
    private val database: MarkerDatabase,
    private val mapper: MarkerModelToEntityMapper
) : MarkerRepo {
    override suspend fun getAllMarkers(): List<MarkerEntity> {
        return database.markerDao().all().map { mapper.mapToEntity(it) }
    }

    override suspend fun insertMarker(marker: MarkerEntity) {
        database.markerDao().insert(mapper.mapToModel(marker))
    }

    override suspend fun deleteMarker(marker: MarkerEntity) {
        database.markerDao().delete(mapper.mapToModel(marker))

    }

    override suspend fun updateMarker(marker: MarkerEntity) {
        database.markerDao().update(mapper.mapToModel(marker))
    }

    override suspend fun updateAllMarkers(list: List<MarkerEntity>) {
        database.markerDao().updateAll(list.map { mapper.mapToModel(it) })
    }
}