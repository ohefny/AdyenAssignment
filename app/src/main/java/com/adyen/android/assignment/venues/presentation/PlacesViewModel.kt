package com.adyen.android.assignment.venues.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.location.domain.GetLocationAvailabilityUpdates
import com.adyen.android.assignment.location.domain.LocationAvailability
import com.adyen.android.assignment.venues.domain.FilterAndSortPlacesUseCase
import com.adyen.android.assignment.venues.domain.FilterOption
import com.adyen.android.assignment.venues.domain.GetNearbyPlacesWithAggregatedInfoUseCase
import com.adyen.android.assignment.venues.domain.SortOption
import com.adyen.android.assignment.venues.presentation.model.PlaceUIState
import com.adyen.android.assignment.venues.presentation.model.toUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.updateAndGet
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val getLocationAvailabilityUpdates: GetLocationAvailabilityUpdates,
    private val getPlacesUseCase: GetNearbyPlacesWithAggregatedInfoUseCase,
    private val filterAndSortPlacesUseCase: FilterAndSortPlacesUseCase,
) : ViewModel() {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val _uiStateStream = MutableStateFlow<PlaceUIState>(getInitialUIState())

    val uiStateStream: StateFlow<PlaceUIState> get() = _uiStateStream
    private val state get() = _uiStateStream.value
    private val TAG = "PlacesViewModel"

    fun initialize() {
        if (state.isInitialized) return
        fetchPlaces()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchPlaces() {
        if (state.isRefreshing || state.isLoading)
            return
        updateState { if (isInitialized) copy(isRefreshing = true) else copy(isLoading = true) }
        getLocationAvailabilityUpdates()
            .filter { it == LocationAvailability.Available }
            .flatMapConcat { getPlacesUseCase() }
            .onEach { info ->
                val ls = info.places
                updateState {
                    copy(
                        places = ls,
                        filteredPlaces = ls,
                        categories = info.availableCategories.map { it.toUIState(false) },
                        isLoading = false,
                        isRefreshing = false,
                        isInitialized = true
                    )
                }
            }.catch {
                //Log.e(TAG, "fetchPlaces: ", it)
                updateState { copy(showError = true, isLoading = false) }
            }.launchIn(viewModelScope)
    }

    fun filterByCategory(categoryId: String) {
        executeFilter(FilterOption.CategoryFilter(categoryId))
    }

    fun filterByVerified() {
        executeFilter(FilterOption.Verified)
    }

    fun filterByOpenNow() {
        executeFilter(FilterOption.OpenNow)
    }

    private fun executeFilter(filter: FilterOption) {
        val oldFilters = state.appliedFilters
        filterAndSortPlacesUseCase(
            places = state.places,
            filters = toggleFilterAndGet(filter),
            sort = state.sortBy,
        ).onEach {
            updateState { copy(filteredPlaces = it) }
        }.catch {
            //Log.e(TAG, "filterByCategory: ", it)
            updateState {
                copy(appliedFilters = oldFilters,
                    categories = categories.map { cat ->
                        val wasSelected =
                            oldFilters.contains(FilterOption.CategoryFilter(cat.category.id))
                        cat.copy(isSelected = wasSelected)
                    })
            } //revert back to old filters
        }.launchIn(viewModelScope)
    }

    private fun toggleFilterAndGet(filter: FilterOption): List<FilterOption> {
        val filters = state.appliedFilters.toMutableList()
        if (filters.contains(filter)) {
            filters.remove(filter)
        } else {
            filters.add(filter)
        }
        updateState {
            copy(appliedFilters = filters, categories = categories.map {
                it.copy(isSelected = filters.contains(FilterOption.CategoryFilter(it.category.id)))
            }.sortedByDescending { it.isSelected })
        }
        return filters
    }

    fun toggleSortBy(sortOption: SortOption) {
        val oldSort = state.sortBy
        val newSort = if (state.sortBy == sortOption) null else sortOption
        updateState { copy(sortBy = newSort) }
        filterAndSortPlacesUseCase(
            places = state.filteredPlaces,
            filters = state.appliedFilters,
            sort = newSort
        ).onEach { updateState { copy(filteredPlaces = it) } }
            .catch {
                //Log.e(TAG, "sortBy: ", it)
                updateState { copy(sortBy = oldSort) } //revert back to old sort
            }.launchIn(viewModelScope)
    }


    private fun getInitialUIState(): PlaceUIState {
        return PlaceUIState(
            places = emptyList(),
            isLoading = false,
            showError = false,
            isRefreshing = false,
            isInitialized = false,
            sortBy = null,
            categories = emptyList(),
            filteredPlaces = emptyList(),
            appliedFilters = emptyList(),
        )
    }

    //update state
    private fun updateState(transform: PlaceUIState.() -> PlaceUIState) {
        _uiStateStream.updateAndGet(transform)
            .also {
                //Log.d(TAG, "updateState: $it")
            }
    }

}
