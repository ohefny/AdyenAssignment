package com.adyen.android.assignment.venues.domain

import com.adyen.android.assignment.core.di.Dispatcher
import com.adyen.android.assignment.core.di.DispatcherKey
import com.adyen.android.assignment.core.miscs.emitFlow
import com.adyen.android.assignment.location.domain.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPlacesUseCase @Inject constructor(
    private val repository: PlacesRepository,
    @Dispatcher(DispatcherKey.IO) private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(latLng: LatLng): Flow<List<Place>> = emitFlow {
        repository.fetchPlaces(latLng)
    }.flowOn(dispatcher)

}