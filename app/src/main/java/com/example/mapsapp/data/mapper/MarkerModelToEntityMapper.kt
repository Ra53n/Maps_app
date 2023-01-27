package com.example.mapsapp.data.mapper

import com.example.mapsapp.data.room.MarkerModel
import com.example.mapsapp.domain.model.MarkerEntity
import com.google.android.gms.maps.model.LatLng

class MarkerModelToEntityMapper {

    fun mapToEntity(model: MarkerModel) = with(model) {
        MarkerEntity(
            id = id,
            latLng = LatLng(lat, lng),
            title = title,
            annotation = annotation
        )
    }

    fun mapToModel(entity: MarkerEntity) = with(entity) {
        MarkerModel(
            id = id,
            lat = latLng.latitude,
            lng = latLng.longitude,
            title = title,
            annotation = annotation
        )
    }
}