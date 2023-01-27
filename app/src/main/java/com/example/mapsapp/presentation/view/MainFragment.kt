package com.example.mapsapp.presentation.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.example.mapsapp.R
import com.example.mapsapp.databinding.MainFragmentBinding
import com.example.mapsapp.presentation.viewModel.MainViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment(R.layout.main_fragment) {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private var map: GoogleMap? = null

    private val viewModel: MainViewModel by viewModel()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        configureMap(googleMap)
        viewModel.requestMyLocation()
        viewModel.getMarkers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        observeLiveData()

        initListeners()
        initMap()
    }

    private fun checkPermissions() {
        if (isPermissionGranted().not()) {
            requestPermissions()
        }
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun initListeners() {
        binding.myLocationFab.setOnClickListener { viewModel.requestMyLocation() }
        binding.editMarkersFab.setOnClickListener { navigateToEditMarkers() }
    }

    private fun navigateToEditMarkers() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MarkersEditingFragment())
            .addToBackStack("")
            .commit()
    }

    private fun isPermissionGranted() =
        checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun configureMap(googleMap: GoogleMap) {
        with(googleMap) {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = false
            setOnMapLongClickListener(::addMarker)
        }
    }

    private fun addMarker(latLng: LatLng) {
        viewModel.onAddMarker(latLng)
        map?.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .position(latLng)
        )
    }

    private fun observeLiveData() {
        viewModel.myLocationLiveData.observe(viewLifecycleOwner) { latLng ->
            viewModel.stopLocationUpdates()
            map?.let {
                it.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
            }

        }

        viewModel.markersLiveData.observe(viewLifecycleOwner) {
            for (marker in it) {
                map?.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker())
                        .position(marker.latLng)
                        .title(marker.title)
                )
            }
        }
    }

    private fun requestPermissions() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            showToast(
                if (permissions.all { it.value }) {
                    getString(R.string.permissions_granted)
                } else {
                    getString(R.string.permissions_not_granted)
                }
            )
        }
            .launch(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION))
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        map?.let {
            it.clear()
            viewModel.getMarkers()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}