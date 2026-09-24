package com.example.khainow.fragments.customer

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.khainow.R
import com.example.khainow.databinding.FragmentHomeBinding
import com.example.khainow.utils.LocationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationHelper: LocationHelper

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            fetchLocationAndNavigate()
        } else {
            Toast.makeText(requireContext(), "Location permission is required to view nearby restaurants", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_restaurantListFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        locationHelper = LocationHelper(requireContext(), requireActivity())

        binding.btnViewRestaurants.setOnClickListener {
            if (locationHelper.hasLocationPermission()) {
                fetchLocationAndNavigate()
            } else {
                locationHelper.requestLocationPermissions(locationPermissionLauncher)
            }
        }

        binding.btnViewFood.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_foodDetailsFragment)
        }

        return binding.root
    }

    private fun fetchLocationAndNavigate() {
        locationHelper.fetchCurrentLocation(
            onSuccess = { lat, lon ->
                Toast.makeText(requireContext(), "Current Location: $lat, $lon", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_homeFragment_to_restaurantListFragment)
            },
            onUnavailable = {
                Toast.makeText(requireContext(), "Location unavailable. Proceeding to restaurants...", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_homeFragment_to_restaurantListFragment)
            },
            onFailure = { e ->
                Toast.makeText(requireContext(), "Location error: ${e.message}", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_homeFragment_to_restaurantListFragment)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
