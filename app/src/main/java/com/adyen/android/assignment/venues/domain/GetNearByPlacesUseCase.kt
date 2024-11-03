package com.adyen.android.assignment.venues.domain

import com.adyen.android.assignment.core.di.Dispatcher
import com.adyen.android.assignment.core.di.DispatcherKey
import com.adyen.android.assignment.location.domain.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNearByPlacesUseCase @Inject constructor(
    private val getPacesUseCase: GetPlacesUseCase,
    private val locationRepository: LocationRepository,
    @Dispatcher(DispatcherKey.IO) private val dispatcher: CoroutineDispatcher
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Place>> = locationRepository.getCurrentLocation()
        .flatMapConcat { getPacesUseCase(it) }
        .map { it.filter { place -> place.photos.isNotEmpty() } }
        .flowOn(dispatcher)

}