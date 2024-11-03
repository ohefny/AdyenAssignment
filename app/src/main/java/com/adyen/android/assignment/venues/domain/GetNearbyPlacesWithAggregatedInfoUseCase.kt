package com.adyen.android.assignment.venues.domain
import com.adyen.android.assignment.core.di.Dispatcher
import com.adyen.android.assignment.core.di.DispatcherKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNearbyPlacesWithAggregatedInfoUseCase @Inject constructor(
    private val getNearbyPlacesUseCase: GetNearByPlacesUseCase,
    @Dispatcher(DispatcherKey.IO) private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<PlacesWithAggregatedInfo> = getNearbyPlacesUseCase()
        .map { places->
            val categoriesToCount= places.flatMap { it.categories }
                .groupBy { it.id }
                .map { CategoryToCount(it.value.first(), it.value.size) }
                .sortedByDescending { it.count }
            val openNowCount = places.count { it.openStatus == ClosedBucket.VERY_LIKELY_OPEN }
            PlacesWithAggregatedInfo(
                places = places,
                availableCategories = categoriesToCount,
                openNowCount = openNowCount
            )
        }
        .flowOn(dispatcher)

}