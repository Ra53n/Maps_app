package com.example.mapsapp.presentation.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment(R.layout.main_fragment) {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    private val viewModel: MainViewModel by viewModel()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        viewModel.requestMyLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        observeLiveData()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun observeLiveData() {
        viewModel.myLocationLiveData.observe(viewLifecycleOwner) {
            viewModel.stopLocationUpdates()
            map.moveCamera(
                CameraUpdateFactory.newLatLng(it)
            )
            map.addMarker(
                MarkerOptions().icon(
                    BitmapDescriptorFactory.defaultMarker()
                ).position(it).title("Ваша позиция")
            )
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    it, 12.0f
                )
            )
        }
    }

    private fun requestPermissions() {
        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.all { it.value }) {
                    Toast.makeText(requireContext(), "Разрешения были выданы", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Разрешения не выданы", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        requestMultiplePermissions.launch(arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}