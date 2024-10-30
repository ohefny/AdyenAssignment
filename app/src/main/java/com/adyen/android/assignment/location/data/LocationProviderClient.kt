package com.adyen.android.assignment.location.data

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.tasks.Task
import javax.inject.Inject

interface LocationProviderClient {
    fun getLastLocation(): Task<Location?>
    fun getLocationAvailability(): Task<LocationAvailability?>
}

class DefaultLocationProviderClient @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationProviderClient {
    @SuppressLint("MissingPermission") // Assume permissions are managed elsewhere
    override fun getLastLocation() : Task<Location?> {
        return fusedLocationProviderClient.lastLocation
    }

    @SuppressLint("MissingPermission") // Assume permissions are managed elsewhere
    override fun getLocationAvailability() : Task<LocationAvailability?> {
        return fusedLocationProviderClient.locationAvailability
    }
}