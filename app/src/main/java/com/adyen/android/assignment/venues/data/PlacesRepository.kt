package com.adyen.android.assignment.venues.data

import com.adyen.android.assignment.venues.data.api.PlacesService
import com.adyen.android.assignment.venues.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.venues.domain.LatLng
import com.adyen.android.assignment.venues.domain.Place
import javax.inject.Inject

// PlacesRepositoryInterface.kt
interface PlacesRepository {
    suspend fun fetchPlaces(latLng: LatLng): List<Place>
}

// PlacesRepository.kt
class DefaultPlacesRepository @Inject constructor(private val placesService: PlacesService) : PlacesRepository {
    override suspend fun fetchPlaces(latLng: LatLng): List<Place> {
        val(latitude, longitude) = latLng
        val params = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude = latitude, longitude = longitude)
            .build()
        return placesService.getVenueRecommendations(params).placeResponses?.map {
            it.toDomain()
        } ?: emptyList()
    }

}








