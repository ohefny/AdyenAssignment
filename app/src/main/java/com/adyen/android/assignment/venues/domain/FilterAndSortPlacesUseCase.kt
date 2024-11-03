package com.adyen.android.assignment.venues.domain

import com.adyen.android.assignment.core.di.Dispatcher
import com.adyen.android.assignment.core.di.DispatcherKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FilterAndSortPlacesUseCase @Inject constructor(
    @Dispatcher(DispatcherKey.COMPUTATION) private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        places: List<Place>,
        filters: List<FilterOption>,
        sort: SortOption? = null
    ): Flow<List<Place>> = flow {
        val (categoryFilters, otherFilters) = filters.partition { it is FilterOption.CategoryFilter }

        val filteredPlaces = places.filter { place ->
            // for category filters, any must match (use "or" logic)
            val matchesCategory = categoryFilters.isEmpty() || categoryFilters.any { it(place) }
            // For other filters, all must match (use "and" logic)
            val matchesOtherFilters = otherFilters.all { it(place) }
            matchesCategory && matchesOtherFilters
        }
        val sortedPlaces = when (sort) {
            SortOption.PRICE -> filteredPlaces.sortedBy { it.price?.id }
            SortOption.DISTANCE -> filteredPlaces.sortedBy { it.distance }
            SortOption.RATING -> filteredPlaces.sortedByDescending { it.rating }
            null -> filteredPlaces
        }
        emit(sortedPlaces)
    }.flowOn(dispatcher)
}

enum class SortOption {
    PRICE, DISTANCE, RATING
}

sealed class FilterOption {
    data object OpenNow : FilterOption() {
        override operator fun invoke(place: Place): Boolean =
            place.openStatus == ClosedBucket.VERY_LIKELY_OPEN
    }

    data object Verified : FilterOption() {
        override operator fun invoke(place: Place): Boolean = place.verified == true
    }

    data class CategoryFilter(val categoryId: String) : FilterOption() {
        override operator fun invoke(place: Place): Boolean =
            place.categories.any { it.id == categoryId }
    }

    abstract operator fun invoke(place: Place): Boolean
}