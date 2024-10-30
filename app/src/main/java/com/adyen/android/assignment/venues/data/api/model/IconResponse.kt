package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IconResponse(
    val prefix: String,
    val suffix: String
)
