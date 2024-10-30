package com.adyen.android.assignment.venues.data

import com.adyen.android.assignment.venues.data.api.model.CategoryResponse
import com.adyen.android.assignment.venues.data.api.model.PhotoResponse
import com.adyen.android.assignment.venues.data.api.model.PlaceResponse
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.ClosedBucket
import com.adyen.android.assignment.venues.domain.IconBucket
import com.adyen.android.assignment.venues.domain.Location
import com.adyen.android.assignment.venues.domain.PhotoBucket
import com.adyen.android.assignment.venues.domain.Place

fun PlaceResponse.toDomain(): Place {
    val location = location?.address ?: location?.country ?: location?.locality ?: ""
    val(lat,lng) = geocode.main
    return Place(
        id = id,
        name = name,
        location =  Location(
            address = location,
            lat = lat,
            lng = lng
        ),
        categories = categories.map { it.toDomain() },
        distance = distance,
        timezone = timezone,
        photos = photos?.map { it.toDomain() }?: emptyList(),
        openStatus = ClosedBucket.fromString(closedBucket)
    )
}
fun PhotoResponse.toDomain(): PhotoBucket {
    return PhotoBucket(
        small = prefix + "300x300" + suffix,
        medium = prefix + "500x500" + suffix,
        large = prefix + "700x700" + suffix
    )
}

fun CategoryResponse.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icons = IconBucket(
            small = iconResponse.prefix + "64" + iconResponse.suffix,
            tiny = iconResponse.prefix + "32" + iconResponse.suffix
        )
    )
}