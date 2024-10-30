package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceResponse(
    @Json(name = "fsq_id")
    val id: String,
    val categories: List<CategoryResponse>,
    val distance: Int,
    @Json(name = "geocodes")
    val geocode: GeoCodeResponse,
    @Json(name = "location")
    val location: LocationResponse?,
    val name: String,
    val timezone: String,
    @Json(name = "closed_bucket")
    val closedBucket:String,
    @Json(name = "related_places")
    val relatedPlaces: RelatedPlaces,
    val photos: List<PhotoResponse>? = null
)

@JsonClass(generateAdapter = true)
data class RelatedPlaces(
    val parent: ParentPlace? = null,
    val children: List<ChildPlace>? = null
)

@JsonClass(generateAdapter = true)
data class ParentPlace(
    @Json(name = "fsq_id")
    val id: String,
    val categories: List<CategoryResponse>,
    val name: String
)

@JsonClass(generateAdapter = true)
data class ChildPlace(
    @Json(name = "fsq_id")
    val id: String,
    val categories: List<CategoryResponse>,
    val name: String
)