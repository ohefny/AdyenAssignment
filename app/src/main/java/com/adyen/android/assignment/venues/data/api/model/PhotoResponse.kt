package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoResponse(
    val id: String,
    @Json(name = "created_at") val createdAt: String,
    val prefix: String,
    val suffix: String,
    val width: Int,
    val height: Int,
    val classifications: List<String>? = null // Optional field
)
