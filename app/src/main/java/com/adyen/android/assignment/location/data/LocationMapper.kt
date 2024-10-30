package com.adyen.android.assignment.location.data

import android.location.Location
import com.adyen.android.assignment.location.domain.LatLng

fun Location.toDomain() = LatLng(
        latitude = latitude,
        longitude = longitude
    )