package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoCodeResponse(
    val main: CoordinateResponse
)

@JsonClass(generateAdapter = true)
data class CoordinateResponse(
    val latitude: Double,
    val longitude: Double,
)