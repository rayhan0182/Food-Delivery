package com.example.khainow.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationHelper(
    private val context: Context,
    private val activity: Activity
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    fun hasLocationPermission(): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineGranted || coarseGranted
    }

    fun requestLocationPermissions(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun fetchCurrentLocation(
        onSuccess: (latitude: Double, longitude: Double) -> Unit,
        onUnavailable: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onSuccess(location.latitude, location.longitude)
                } else {
                    onUnavailable()
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
        } catch (e: SecurityException) {
            onFailure(e)
        }
    }
}
