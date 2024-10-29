package com.adyen.android.assignment.venues.data

import com.adyen.android.assignment.venues.data.api.PlacesService
import com.adyen.android.assignment.venues.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.venues.data.api.model.Place
import javax.inject.Inject

// PlacesRepositoryInterface.kt
interface PlacesRepository {
    suspend fun fetchPlaces(latitude:Double,longitude:Double): List<Place>
}

// PlacesRepository.kt
class DefaultPlacesRepository @Inject constructor(private val placesService: PlacesService) : PlacesRepository {
    override suspend fun fetchPlaces(latitude:Double,longitude:Double): List<Place> {
        val params = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude = latitude, longitude = longitude)
            .build()
        val response = placesService.getVenueRecommendations(params).execute()
        if (!response.isSuccessful) {
            throw response.errorBody()?.let { Exception(it.string()) } ?: Exception("Unknown error")
        } else {
            val body = response.body()
            if (body == null) {
                throw Exception("Response body is null")
            } else {
                return body.places ?: emptyList()
            }
        }
    }
}








