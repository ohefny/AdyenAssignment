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
    val photos: List<PhotoResponse>? = null,
    val rating: Double? = null,
    val verified: Boolean? = null,
    val price: Int? = null,
    val hours: HoursResponse? = null
)

@JsonClass(generateAdapter = true)
data class HoursResponse(
    @Json(name = "display")
    val display: String?,
    @Json(name = "is_local_holiday")
    val isLocalHoliday: Boolean,
    @Json(name = "open_now")
    val openNow: Boolean
)