package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseWrapper(
    @Json(name = "results")
    val places: List<Place>?,
)
