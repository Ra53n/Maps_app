package com.example.mapsapp.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapsapp.R
import com.example.mapsapp.databinding.MarkersEditingFragmentBinding
import com.example.mapsapp.presentation.view.adapter.MarkerEditingAdapter
import com.example.mapsapp.presentation.viewModel.MarkersEditingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarkersEditingFragment : Fragment(R.layout.markers_editing_fragment) {

    private var _binding: MarkersEditingFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarkersEditingViewModel by viewModel()

    private val adapter by lazy {
        MarkerEditingAdapter(
            onDeleteMarkerClick = { marker -> viewModel.onDeleteMarker(marker) },
            onSaveMarkerClick = { marker -> viewModel.saveMarker(marker) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MarkersEditingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        binding.markersRV.adapter = adapter
        viewModel.getMarkers()
    }

    private fun observeLiveData() {
        viewModel.markersLiveData.observe(viewLifecycleOwner) {
            adapter.setItems(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}