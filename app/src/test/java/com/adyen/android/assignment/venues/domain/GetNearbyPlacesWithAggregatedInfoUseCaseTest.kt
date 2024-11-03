package com.adyen.android.assignment.venues.domain

import com.adyen.android.assignment.venues.Mocker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetNearbyPlacesWithAggregatedInfoUseCaseTest {

    private lateinit var getNearbyPlacesWithAggregatedInfoUseCase: GetNearbyPlacesWithAggregatedInfoUseCase
    private val dispatcher = UnconfinedTestDispatcher()
    private val getNearbyPlacesUseCase: GetNearByPlacesUseCase = mock()

    @Before
    fun setUp() {
        getNearbyPlacesWithAggregatedInfoUseCase = GetNearbyPlacesWithAggregatedInfoUseCase(getNearbyPlacesUseCase, dispatcher)
    }

    @Test
    fun `aggregate categories should return correct category counts and categories are sorted descendingly by count`() = runTest {

        val category1 = Mocker.getFakeCategory().copy(id = "1")
        val category2 = Mocker.getFakeCategory().copy(id = "2")
        val places = listOf(
            Mocker.getFakePlace().copy(categories = listOf(category1,category2)),
            Mocker.getFakePlace().copy(categories = listOf(category1))
        )
        whenever(getNearbyPlacesUseCase.invoke()).thenReturn(flowOf(places))
        val result = getNearbyPlacesWithAggregatedInfoUseCase().first()

        assertEquals(2, result.availableCategories.size)
        assertEquals("1", result.availableCategories.first().category.id)
        assertEquals(2, result.availableCategories[0].count)
        assertEquals(1, result.availableCategories[1].count)
    }

    @Test
    fun `aggregate open status should return correct open now count`() = runTest {
        val places = listOf(
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_OPEN),
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_CLOSED),
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_OPEN),
            )
        whenever(getNearbyPlacesUseCase.invoke()).thenReturn(flowOf(places))

        val result = getNearbyPlacesWithAggregatedInfoUseCase().first()

        assertEquals(2, result.openNowCount)
    }

    @Test
    fun `aggregate empty places list should return empty categories and zero open now count`() = runTest {
        val places = emptyList<Place>()
        whenever(getNearbyPlacesUseCase.invoke()).thenReturn(flowOf(places))
        val result = getNearbyPlacesWithAggregatedInfoUseCase().first()

        assertEquals(0, result.availableCategories.size)
        assertEquals(0, result.openNowCount)
    }

    @Test
    fun `aggregate places with no categories should return empty categories`() = runTest {
        val places = listOf(
            Mocker.getFakePlace().copy(categories = emptyList())
        )
        whenever(getNearbyPlacesUseCase.invoke()).thenReturn(flowOf(places))
        val result = getNearbyPlacesWithAggregatedInfoUseCase().first()

        assertEquals(0, result.availableCategories.size)
    }
}