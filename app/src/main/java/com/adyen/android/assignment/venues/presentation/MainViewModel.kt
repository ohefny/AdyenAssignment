package com.adyen.android.assignment.venues.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.core.miscs.ConsumableCommand
import com.adyen.android.assignment.location.domain.LocationAvailability
import com.adyen.android.assignment.location.domain.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.updateAndGet
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val locationRepository: LocationRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(getInitialState())
    val uiState: StateFlow<MainUIState> = _uiState

    init {
        Log.d("MainViewModel", "init ${hashCode()}")
        //getCurrentLocation()
    }

    private fun getCurrentLocation() {
        locationRepository.getCurrentLocation().onEach {
            Log.d("MainViewModel", "Location: $it")
        }.catch {
            Log.e("MainViewModel", "Error getting location", it)
        }.launchIn(viewModelScope)
    }

    private fun checkLocationAvailability() {
        locationRepository.getLocationAvailability().onEach {
            if (it == LocationAvailability.Unavailable) {
                updateState {
                    copy(
                        showLocationMightBeOff = ConsumableCommand(Unit),
                        locationAvailability = it
                    )
                }
            } else updateState { copy(locationAvailability = it) }
            Log.d("MainViewModel", "Location availability: $it")
        }.filter { it == LocationAvailability.Available }
            .onEach { getCurrentLocation() }
            .catch {
            Log.e("MainViewModel", "Error getting location availability", it)
        }.launchIn(viewModelScope)
    }

    fun onPermissionDenied() {
        updateState { copy(showPermissionNeeded = ConsumableCommand(PermissionDenialStatus.DeniedForever)) }
    }

    fun onPermissionNeedsRational() {
        updateState { copy(showPermissionNeeded = ConsumableCommand(PermissionDenialStatus.CanAskAgain)) }
    }

    private fun updateState(transform: MainUIState.() -> MainUIState) {
        _uiState.updateAndGet(transform).also {
            Log.d("MainViewModel", "State: $it")
        }
    }

    private fun getInitialState(): MainUIState = MainUIState(null, null, null)

    fun onPermissionGranted() {
        checkLocationAvailability()
    }


}

data class MainUIState(
    val showPermissionNeeded: ConsumableCommand<PermissionDenialStatus>?,
    val showLocationMightBeOff: ConsumableCommand<Unit>?,
    val locationAvailability: LocationAvailability?
)

enum class PermissionDenialStatus {
    CanAskAgain,
    DeniedForever,
    Unknown
}