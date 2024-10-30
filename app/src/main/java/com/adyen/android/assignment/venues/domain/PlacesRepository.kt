package com.adyen.android.assignment.venues.domain

import com.adyen.android.assignment.location.domain.LatLng

interface PlacesRepository {
    suspend fun fetchPlaces(latLng: LatLng): List<Place>
}