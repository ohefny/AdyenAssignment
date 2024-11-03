package com.adyen.android.assignment.venues.domain

import com.adyen.android.assignment.venues.Mocker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FilterAndSortPlacesUseCaseTest {

    private lateinit var filterAndSortPlacesUseCase: FilterAndSortPlacesUseCase
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        filterAndSortPlacesUseCase = FilterAndSortPlacesUseCase(dispatcher)
    }

    @Test
    fun `empty places list should return empty list`() = runTest {
        val places = emptyList<Place>()
        val filters = listOf(FilterOption.CategoryFilter("1"))

        val result = filterAndSortPlacesUseCase(places, filters).first()

        assertTrue(result.isEmpty())
    }


    @Test
    fun `filter by category`() = runTest {
        val fakeCategoryWithId1 = Mocker.getFakeCategory().copy(id = "1")
        val differentCategory = Mocker.getFakeCategory().copy(id = "2")
        val places = listOf(
            Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId1)),
            Mocker.getFakePlace().copy(categories = listOf(differentCategory))
        )
        val filters = listOf(FilterOption.CategoryFilter("1"))

        val result = filterAndSortPlacesUseCase(places, filters).first()

        assertEquals(1, result.size)
        assertEquals("1", result.first().categories.first().id)
    }

    @Test
    fun `filter by open now`() = runTest {
        val places = listOf(
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_OPEN),
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_CLOSED)
        )
        val filters = listOf(FilterOption.OpenNow)

        val result = filterAndSortPlacesUseCase(places, filters).first()

        assertEquals(1, result.size)
        assertEquals(ClosedBucket.VERY_LIKELY_OPEN, result.first().openStatus)
    }

    @Test
    fun `sort by price`() = runTest {
        val places = listOf(
            Mocker.getFakePlace().copy(price = PriceBucket.MODERATE),
            Mocker.getFakePlace().copy(price = PriceBucket.CHEAP)
        )
        val sortOption = SortOption.PRICE

        val result = filterAndSortPlacesUseCase(
            places = places,
            filters = emptyList(),
            sort = sortOption
        ).first()

        assertEquals(PriceBucket.CHEAP, result.first().price)
    }

    @Test
    fun `sort by rating`() = runTest {
        val places = listOf(
            Mocker.getFakePlace().copy(rating = 4.5),
            Mocker.getFakePlace().copy(rating = 3.5)
        )
        val sortOption = SortOption.RATING

        val result = filterAndSortPlacesUseCase(places, emptyList(), sortOption).first()

        assertEquals(4.5, result.first().rating!!, 0.0)
    }

    @Test
    fun `filter by category and sort by distance should return sorted places by distance ascending that includes that category`() = runTest {
        val fakeCategory = Mocker.getFakeCategory().copy(id = "1")
        val differentCategory = Mocker.getFakeCategory().copy(id = "2")
        val places = listOf(
            Mocker.getFakePlace().copy(categories = listOf(fakeCategory), distance = 200),
            Mocker.getFakePlace().copy(categories = listOf(differentCategory), distance = 300),
            Mocker.getFakePlace().copy(categories = listOf(fakeCategory), distance = 100),
        )
        val filters = listOf(FilterOption.CategoryFilter("1"))
        val sortOption = SortOption.DISTANCE

        val result = filterAndSortPlacesUseCase(places, filters, sortOption).first()
        assertEquals(2, result.size)
        assertEquals(100, result.first().distance)
    }

    //filter by multiple categories
    @Test
    fun `filter by multiple categories should combine all places with any category`() = runTest {
        val fakeCategoryWithId1 = Mocker.getFakeCategory().copy(id = "1")
        val fakeCategoryWithId2 = Mocker.getFakeCategory().copy(id = "2")
        val fakeCategoryWithId3 = Mocker.getFakeCategory().copy(id = "3")

        val placeWithCategory1 = Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId1))
        val placeWithCategory2 = Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId2))
        val placeWithCategory3 = Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId3))
        val placeWithCategory1And2 = Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId1, fakeCategoryWithId2))

        val places = listOf(
            placeWithCategory1,
            placeWithCategory2,
            placeWithCategory3,
            placeWithCategory1And2,
        )
        val filters = listOf(FilterOption.CategoryFilter("1"), FilterOption.CategoryFilter("2"))

        val result = filterAndSortPlacesUseCase(places, filters).first()

        assertEquals(3, result.size)
        for (place in result) {
            val containCategory1Or2 = place.categories.contains(fakeCategoryWithId1) || place.categories.contains(fakeCategoryWithId2)
            assertTrue(containCategory1Or2)
        }
    }
    @Test
    fun `empty filter should return same list`() = runTest {
        val fakeCategoryWithId1 = Mocker.getFakeCategory().copy(id = "1")

        val places = listOf(
            Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId1)),
            Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId1)),
            Mocker.getFakePlace().copy(categories = listOf(fakeCategoryWithId1)),
        )
        val filters = emptyList<FilterOption>()

        val result = filterAndSortPlacesUseCase(places, filters).first()

        assertEquals(3, result.size)
    }

    @Test
    fun `filter with no matches should return empty list`() = runTest {
        val category1 = Mocker.getFakeCategory().copy(id = "1")
        val places = listOf(
            Mocker.getFakePlace().copy(categories = listOf(category1)),
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_CLOSED)
        )
        val filters = listOf(FilterOption.CategoryFilter("2"))

        val result = filterAndSortPlacesUseCase(places, filters).first()

        assertTrue(result.isEmpty())
    }


    @Test
    fun `multiple filters with no match should return empty list`() = runTest {
        val places = listOf(
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_OPEN, verified = false),
            Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_CLOSED, verified = true)
        )
        val filters = listOf(FilterOption.OpenNow, FilterOption.Verified)

        val result = filterAndSortPlacesUseCase(places, filters).first()

        assertTrue(result.isEmpty())
    }


}