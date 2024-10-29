package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoCodeResponse(
    val main: Coordinate
)

@JsonClass(generateAdapter = true)
data class Coordinate(
    val latitude: Double,
    val longitude: Double,
)