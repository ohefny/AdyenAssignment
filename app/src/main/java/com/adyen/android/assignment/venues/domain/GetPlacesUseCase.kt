package com.adyen.android.assignment.venues.domain

import android.util.Log
import com.adyen.android.assignment.core.di.Dispatcher
import com.adyen.android.assignment.core.di.DispatcherKey
import com.adyen.android.assignment.venues.data.PlacesRepository
import com.adyen.android.assignment.venues.data.api.model.Place
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

// GetPlacesUseCase.kt
class GetPlacesUseCase @Inject constructor(private val repository: PlacesRepository,
                                           @Dispatcher(DispatcherKey.IO) private val dispatcher: CoroutineDispatcher) {
    operator fun invoke(): Flow<List<Place>> {
        return flow {
            emit(repository.fetchPlaces(52.376510, 4.905890))
        }.onEach {
            Log.d("GetPlacesUseCase", "invoke: $it")
        }.flowOn(dispatcher)
    }
}
