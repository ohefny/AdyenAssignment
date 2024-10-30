package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationResponse(
    val address: String?,
    val country: String?,
    val locality: String?,
    val postcode: String?,
)
