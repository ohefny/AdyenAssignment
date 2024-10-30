package com.adyen.android.assignment.venues.data


import com.adyen.android.assignment.location.domain.LatLng
import com.adyen.android.assignment.venues.Mocker
import com.adyen.android.assignment.venues.data.api.PlacesService
import com.adyen.android.assignment.venues.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.venues.data.api.model.PlaceResponse
import com.adyen.android.assignment.venues.data.api.model.ResponseWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultPlaceRepositoryTest {

    private lateinit var placesService: PlacesService
    private lateinit var repository: DefaultPlacesRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        placesService = mock()
        repository = DefaultPlacesRepository(placesService)
    }

    @Test
    fun `fetchPlaces should return list of mapped places`() = testScope.runTest {
        val latLng = LatLng(52.3676, 4.9041)
        val expectedPlaces = listOf(Mocker.getFakePlaceResponse(), Mocker.getFakePlaceResponse())
        val responseWrapper = ResponseWrapper(expectedPlaces)
        val queryBuilder = VenueRecommendationsQueryBuilder().setLatitudeLongitude(latLng.latitude, latLng.longitude)

        whenever(placesService.getVenueRecommendations(queryBuilder.build())).thenReturn(responseWrapper)

        val result = repository.fetchPlaces(latLng)
        val expectedMappedPlaces = expectedPlaces.map { it.toDomain() }

        assertEquals(/* expected = */ expectedMappedPlaces, /* actual = */ result)
    }

    @Test
    fun `fetchPlaces should return empty list when no places found`() = testScope.runTest {
        val latLng = LatLng(52.3676, 4.9041)
        val expectedPlaces = emptyList<PlaceResponse>()
        val responseWrapper = ResponseWrapper(expectedPlaces)
        val queryBuilder = VenueRecommendationsQueryBuilder().setLatitudeLongitude(latLng.latitude, latLng.longitude)

        whenever(placesService.getVenueRecommendations(queryBuilder.build())).thenReturn(responseWrapper)

        val result = repository.fetchPlaces(latLng)

        assertEquals(expectedPlaces, result)
    }

    @Test
    fun `fetchPlaces should throw exception when service fails`() = testScope.runTest {
        val latLng = LatLng(52.3676, 4.9041)
        val exception = RuntimeException("Error fetching places")
        val queryBuilder = VenueRecommendationsQueryBuilder().setLatitudeLongitude(latLng.latitude, latLng.longitude)

        whenever(placesService.getVenueRecommendations(queryBuilder.build())).thenThrow(exception)

        var error: Throwable? = null
        try {
            repository.fetchPlaces(latLng)
        } catch (e: Throwable) {
            error = e
        }

        assertEquals(exception, error)
    }

    @Test
    fun `fetchPlaces should not call service again for same LatLng`() = testScope.runTest {
        val latLng = LatLng(52.3676, 4.9041)
        val expectedPlaces = listOf(Mocker.getFakePlaceResponse(), Mocker.getFakePlaceResponse())
        val responseWrapper = ResponseWrapper(expectedPlaces)
        val queryBuilder = VenueRecommendationsQueryBuilder().setLatitudeLongitude(latLng.latitude, latLng.longitude)

        whenever(placesService.getVenueRecommendations(queryBuilder.build())).thenReturn(responseWrapper)

        // First call
        repository.fetchPlaces(latLng)
        // Second call with the same LatLng
        repository.fetchPlaces(latLng)

        // Verify that the service is called only once
        verify(placesService, times(1)).getVenueRecommendations(queryBuilder.build())
    }
}