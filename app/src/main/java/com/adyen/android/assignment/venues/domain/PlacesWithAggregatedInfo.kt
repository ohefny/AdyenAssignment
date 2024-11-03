package com.adyen.android.assignment.venues.domain

data class PlacesWithAggregatedInfo(
    val places: List<Place>,
    val availableCategories: List<CategoryToCount>,
    val openNowCount: Int,
)
    data class CategoryToCount(
        val category: Category,
        val count: Int
    )