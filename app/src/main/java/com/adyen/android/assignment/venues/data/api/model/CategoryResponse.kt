package com.adyen.android.assignment.venues.data.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponse(
    val icon: Icon,
    val id: String,
    val name: String,
)
