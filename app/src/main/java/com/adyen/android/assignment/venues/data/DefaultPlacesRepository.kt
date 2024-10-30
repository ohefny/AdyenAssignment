package com.adyen.android.assignment.venues.data

import com.adyen.android.assignment.location.domain.LatLng
import com.adyen.android.assignment.venues.data.api.PlacesService
import com.adyen.android.assignment.venues.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.venues.domain.Place
import com.adyen.android.assignment.venues.domain.PlacesRepository
import javax.inject.Inject

class DefaultPlacesRepository @Inject constructor(private val placesService: PlacesService) :
    PlacesRepository {
    private var cachedPlaces: PlacesToLatLng? = null

    override suspend fun fetchPlaces(latLng: LatLng): List<Place> {
        if (cachedPlaces?.latLng==latLng)
            return cachedPlaces!!.places

        val(latitude, longitude) = latLng
        val params = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(latitude = latitude, longitude = longitude)
            .build()
        val places =  placesService.getVenueRecommendations(params).placeResponses?.map {
            it.toDomain()
        }
        if (places != null)
            cachedPlaces = PlacesToLatLng(places, latLng)
        return places ?: emptyList()
    }

    data class PlacesToLatLng(val places: List<Place>, val latLng: LatLng)

}








