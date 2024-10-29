package com.adyen.android.assignment.venues.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.venues.data.api.model.PlaceResponse
import com.adyen.android.assignment.venues.domain.GetPlacesUseCase
import com.adyen.android.assignment.venues.domain.LatLng
import com.adyen.android.assignment.venues.domain.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(private val getPlacesUseCase: GetPlacesUseCase) :
    ViewModel() {
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val uiState: StateFlow<List<Place>> get() = _places
    private val TAG = "PlacesViewModel"
    init {
        fetchPlaces()
    }

    private fun fetchPlaces() {
        getPlacesUseCase(LatLng(52.3676, 4.9041))
            .onEach { _places.value = it }
            .catch { Log.e(TAG, "fetchPlaces: ", it) }
            .launchIn(viewModelScope)
    }

}
