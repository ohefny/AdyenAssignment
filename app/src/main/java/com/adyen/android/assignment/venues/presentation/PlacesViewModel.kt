package com.adyen.android.assignment.venues.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.location.domain.LocationAvailability
import com.adyen.android.assignment.location.domain.GetLocationAvailabilityUpdates
import com.adyen.android.assignment.venues.domain.GetPlacesAtCurrentLocationUseCase
import com.adyen.android.assignment.venues.domain.Place
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
    private val getPlacesUseCase: GetPlacesAtCurrentLocationUseCase
) :
    ViewModel() {
    private val _places = MutableStateFlow<PlaceUIState>(getInitialUIState())
    val uiState: StateFlow<PlaceUIState> get() = _places
    private val TAG = "PlacesViewModel"

    init {
        fetchPlaces()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchPlaces() {
        updateState { copy(showLoading = true) }
        getLocationAvailabilityUpdates()
            .filter { it == LocationAvailability.Available }
            .flatMapConcat { getPlacesUseCase() }
            .onEach {
                updateState {
                    copy(
                        places = it.filter { it.photos.isNotEmpty() },
                        noPhotosPlaces = places.filter { it.photos.isEmpty() },
                        showLoading = false
                    )
                }
            }
            .catch {
                Log.e(TAG, "fetchPlaces: ", it)
                updateState { copy(showError = true) }
            }.launchIn(viewModelScope)
    }

    private fun getInitialUIState(): PlaceUIState {
        return PlaceUIState(
            places = emptyList(),
            noPhotosPlaces = emptyList(),
            showLoading = false,
            showError = false
        )
    }

    //update state
    private fun updateState(transform: PlaceUIState.() -> PlaceUIState) {
        _places.updateAndGet(transform)
            .also { Log.d(TAG, "updateState: $it") }
    }


    data class PlaceUIState(
        val noPhotosPlaces: List<Place>,
        val places: List<Place>,
        val showLoading: Boolean,
        val showError: Boolean
    )

}
