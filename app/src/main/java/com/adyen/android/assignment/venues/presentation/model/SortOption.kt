package com.adyen.android.assignment.venues.presentation.model

import androidx.compose.runtime.Immutable
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.CategoryToCount
import com.adyen.android.assignment.venues.domain.FilterOption
import com.adyen.android.assignment.venues.domain.Place
import com.adyen.android.assignment.venues.domain.SortOption


@Immutable
data class PlaceUIState(
    val appliedFilters: List<FilterOption>,
    val sortBy: SortOption?,
    val places: List<Place>,
    val categories: List<CategoryUIState>,
    val filteredPlaces: List<Place>,
    val isLoading: Boolean,
    val showError: Boolean,
    val isRefreshing: Boolean,
    val isInitialized: Boolean
)

@Immutable
data class CategoryUIState(
    val category: Category,
    val count: Int,
    val isSelected: Boolean
) {
    val name: String get() = category.name
}

fun CategoryToCount.toUIState(isSelected: Boolean) = CategoryUIState(category, count, isSelected)