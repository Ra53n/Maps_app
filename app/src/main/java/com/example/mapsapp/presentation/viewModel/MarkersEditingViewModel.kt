package com.example.mapsapp.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.data.repo.MarkerRepo
import com.example.mapsapp.domain.model.MarkerEntity
import kotlinx.coroutines.launch

class MarkersEditingViewModel(
    private val repo: MarkerRepo
) : ViewModel() {

    private val _markersLiveData = MutableLiveData<List<MarkerEntity>>()
    val markersLiveData: LiveData<List<MarkerEntity>> by this::_markersLiveData


    fun getMarkers() {
        viewModelScope.launch {
            val markers = repo.getAllMarkers()
            _markersLiveData.postValue(markers)
        }
    }

    fun onDeleteMarker(marker: MarkerEntity) {
        viewModelScope.launch {
            repo.deleteMarker(marker)
            getMarkers()
        }
    }

    fun saveMarker(marker: MarkerEntity) {
        viewModelScope.launch {
            repo.updateMarker(marker)
            getMarkers()
        }
    }
}