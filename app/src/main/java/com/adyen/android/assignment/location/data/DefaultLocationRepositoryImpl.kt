package com.adyen.android.assignment.location.data

import android.location.Location
import com.adyen.android.assignment.core.miscs.asFlow
import com.adyen.android.assignment.location.domain.LatLng
import com.adyen.android.assignment.location.domain.LocationAvailability
import com.adyen.android.assignment.location.domain.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultLocationRepository @Inject constructor(
    private val fusedLocationProviderClient: LocationProviderClient
) : LocationRepository {

    override fun getCurrentLocation(): Flow<LatLng> =
        fusedLocationProviderClient.getLastLocation()
            .asFlow()
            .map(Location::toDomain)

    override fun getLocationAvailability(): Flow<LocationAvailability> =
        fusedLocationProviderClient.getLocationAvailability().asFlow()
            .map {
                if (it.isLocationAvailable) LocationAvailability.Available
                else LocationAvailability.Unavailable
            }.catch {
                if (it is SecurityException) emit(LocationAvailability.PermissionNeeded)
                else emit(LocationAvailability.Unavailable)
            }


}