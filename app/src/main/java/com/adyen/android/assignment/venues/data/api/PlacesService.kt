package com.adyen.android.assignment.venues.data.api

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.venues.data.api.model.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap


interface PlacesService {
    /**
     * Get venue recommendations.
     *
     * See [the docs](https://developer.foursquare.com/reference/places-nearby)
     */
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("places/search")
    suspend fun getVenueRecommendations(@QueryMap query: Map<String, String>): ResponseWrapper

}
