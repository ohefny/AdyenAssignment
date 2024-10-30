package com.adyen.android.assignment.location.domain

import com.adyen.android.assignment.core.di.Dispatcher
import com.adyen.android.assignment.core.di.DispatcherKey
import com.adyen.android.assignment.core.miscs.tickerFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetLocationAvailabilityUpdates @Inject constructor(private val locationRepository: LocationRepository,//dispatcher
                                                         @Dispatcher(DispatcherKey.COMPUTATION) private val dispatcher: CoroutineDispatcher
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<LocationAvailability> = tickerFlow(5.toDuration(DurationUnit.SECONDS))
        .flatMapLatest { locationRepository.getLocationAvailability() }
        .distinctUntilChanged()
        .flowOn(dispatcher)

}