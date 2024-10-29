package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Place(
    @Json(name = "fsq_id")
    val id: String,
    val categories: List<Category>,
    val distance: Int,
    @Json(name = "geocodes")
    val geocode: GeoCode,
    val location: Location,
    val name: String,
    val timezone: String,
    @Json(name = "related_places")
    val relatedPlaces: RelatedPlaces
)

@JsonClass(generateAdapter = true)
data class RelatedPlaces(
    val parent: ParentPlace? = null,
    val children: List<ChildPlace>? = null
)

@JsonClass(generateAdapter = true)
data class ParentPlace(
    val fsq_id: String,
    val categories: List<Category>,
    val name: String
)
@JsonClass(generateAdapter = true)
data class ChildPlace(
    val fsq_id: String,
    val categories: List<Category>,
    val name: String
)