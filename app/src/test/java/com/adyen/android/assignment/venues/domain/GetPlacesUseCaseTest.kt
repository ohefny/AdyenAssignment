package com.adyen.android.assignment.venues.domain

import com.adyen.android.assignment.venues.Mocker
import com.adyen.android.assignment.venues.data.PlacesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetPlacesUseCaseTest {

    private lateinit var repository: PlacesRepository
    private lateinit var getPlacesUseCase: GetPlacesUseCase
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        repository = mock()

        getPlacesUseCase = GetPlacesUseCase(repository, testDispatcher)
    }

    @Test
    fun `invoke should return list of places`() = testScope.runTest {
        val latLng = LatLng(52.3676, 4.9041)
        val expectedPlaces = listOf(Mocker.getFakePlace(), Mocker.getFakePlace())
        val argumentCaptor = argumentCaptor<LatLng>()
        whenever(repository.fetchPlaces(latLng)).thenReturn(expectedPlaces)
        val result = getPlacesUseCase(latLng).first()
        verify(repository).fetchPlaces(argumentCaptor.capture())
        assertEquals(/* expected = */ latLng,/* actual = */ argumentCaptor.firstValue )
        assertEquals(/* expected = */ expectedPlaces, /* actual = */ result)
    }

    @Test
    fun `invoke should return empty list when no places found`() = testScope.runTest {
        val latLng = LatLng(52.3676, 4.9041)
        val expectedPlaces = emptyList<Place>()

        whenever(repository.fetchPlaces(latLng)).thenReturn(expectedPlaces)

        val result = getPlacesUseCase(latLng).first()

        assertEquals(expectedPlaces, result)
    }

    @Test
    fun `invoke should emit error when repository throws exception`() = testScope.runTest {
        val latLng = LatLng(52.3676, 4.9041)
        val exception = RuntimeException("Error fetching places")
        whenever(repository.fetchPlaces(latLng)).thenThrow(exception)

        var error: Throwable? = null
        val result = getPlacesUseCase(latLng).catch { error = it }.firstOrNull()
        assertEquals(/* expected = */ exception, /* actual = */ error)
        assertEquals(/* expected = */ null, /* actual = */ result)
    }

}