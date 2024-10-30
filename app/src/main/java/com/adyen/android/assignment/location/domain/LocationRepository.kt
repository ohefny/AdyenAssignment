package com.adyen.android.assignment.location.domain

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentLocation(): Flow<LatLng>
    fun getLocationAvailability(): Flow<LocationAvailability>
}

enum class LocationAvailability {
    Available,
    PermissionNeeded,
    Unavailable
}