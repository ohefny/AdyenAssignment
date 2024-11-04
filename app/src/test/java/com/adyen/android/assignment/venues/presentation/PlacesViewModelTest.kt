package com.adyen.android.assignment.venues.presentation

import com.adyen.android.assignment.location.domain.GetLocationAvailabilityUpdates
import com.adyen.android.assignment.location.domain.LocationAvailability
import com.adyen.android.assignment.venues.Mocker
import com.adyen.android.assignment.venues.domain.CategoryToCount
import com.adyen.android.assignment.venues.domain.ClosedBucket
import com.adyen.android.assignment.venues.domain.FilterAndSortPlacesUseCase
import com.adyen.android.assignment.venues.domain.FilterOption
import com.adyen.android.assignment.venues.domain.GetNearbyPlacesWithAggregatedInfoUseCase
import com.adyen.android.assignment.venues.domain.Place
import com.adyen.android.assignment.venues.domain.PlacesWithAggregatedInfo
import com.adyen.android.assignment.venues.domain.PriceBucket
import com.adyen.android.assignment.venues.domain.SortOption
import com.adyen.android.assignment.venues.presentation.model.PlaceUIState
import com.adyen.android.assignment.venues.presentation.model.toUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.internal.verification.Times
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class PlacesViewModelTest {

    private val getLocationAvailabilityUpdates: GetLocationAvailabilityUpdates = mock()
    private val getPlacesUseCase: GetNearbyPlacesWithAggregatedInfoUseCase = mock()
    private val dispatcher = UnconfinedTestDispatcher()
    private val filterAndSortPlacesUseCase: FilterAndSortPlacesUseCase = FilterAndSortPlacesUseCase(dispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined) // Set the main dispatcher for tests
    }

    //verify fetch places should be called once vm initialize is called
    @Test
    fun `fetch places should be called once vm initialized`() = runTest {
        val places = listOf(Mocker.getFakePlace())
        val categories = listOf(Mocker.getFakeCategory())
        val availableCategoriesToCount = categories.map { CategoryToCount(it, 1) }
        val aggregatedInfo = PlacesWithAggregatedInfo(places, availableCategoriesToCount, 1)

        whenever(getLocationAvailabilityUpdates()).thenReturn(flowOf(LocationAvailability.Available))
        whenever(getPlacesUseCase()).thenReturn(flowOf(aggregatedInfo))
        val vm = spy(
            PlacesViewModel(
                getLocationAvailabilityUpdates,
                getPlacesUseCase,
                filterAndSortPlacesUseCase
            )
        )
        vm.initialize()
        verify(vm).fetchPlaces()
    }

    @Test
    fun `initialize should update state with fetched places and categories`() = runTest {
        val places = listOf(Mocker.getFakePlace())
        val categories = listOf(Mocker.getFakeCategory())
        val availableCategoriesToCount = categories.map { CategoryToCount(it, 1) }
        val aggregatedInfo = PlacesWithAggregatedInfo(places, availableCategoriesToCount, 1)


        whenever(getLocationAvailabilityUpdates()).thenReturn(flowOf(LocationAvailability.Available))
        whenever(getPlacesUseCase()).thenReturn(flowOf(aggregatedInfo))
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )
        viewModel.initialize()
        val state = viewModel.uiStateStream.first()
        assertEquals(places, state.places)
        assertEquals(availableCategoriesToCount.map { it.toUIState(false) }, state.categories)
        assertFalse(state.isLoading)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun `fetch places should call getLocationAvailabilityUpdates and getPlacesUseCase`() = runTest {
        val places = listOf(Mocker.getFakePlace())
        val categories = listOf(Mocker.getFakeCategory())
        val availableCategoriesToCount = categories.map { CategoryToCount(it, 1) }
        val aggregatedInfo = PlacesWithAggregatedInfo(places, availableCategoriesToCount, 1)

        whenever(getLocationAvailabilityUpdates()).thenReturn(flowOf(LocationAvailability.Available))
        whenever(getPlacesUseCase()).thenReturn(flowOf(aggregatedInfo))
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )

        viewModel.fetchPlaces()

        verify(getLocationAvailabilityUpdates).invoke()
        verify(getPlacesUseCase).invoke()
    }

    @Test
    fun `initialize should not call fetch places if ui state is already initialized`() = runTest {
        val places = listOf(Mocker.getFakePlace())
        val categories = listOf(Mocker.getFakeCategory())
        val availableCategoriesToCount = categories.map { CategoryToCount(it, 1) }
        val aggregatedInfo = PlacesWithAggregatedInfo(places, availableCategoriesToCount, 1)

        whenever(getLocationAvailabilityUpdates()).thenReturn(flowOf(LocationAvailability.Available))
        whenever(getPlacesUseCase()).thenReturn(flowOf(aggregatedInfo))
        val viewModel = spy(
            PlacesViewModel(
                getLocationAvailabilityUpdates,
                getPlacesUseCase,
                filterAndSortPlacesUseCase
            )
        )

        viewModel.initialize()
        viewModel.initialize()
        verify(viewModel, Times(1)).fetchPlaces()
        verify(getPlacesUseCase, Times(1)).invoke()
        verify(getLocationAvailabilityUpdates, Times(1)).invoke()
    }


    @Test
    fun `applying category filter should update uiState with applied filters`() = runTest {
        val category = CategoryToCount(Mocker.getFakeCategory(), 1)
        val aggregatedInfo = PlacesWithAggregatedInfo(emptyList(), listOf(category), 1)
        whenever(getLocationAvailabilityUpdates()).thenReturn(flowOf(LocationAvailability.Available))
        whenever(getPlacesUseCase()).thenReturn(flowOf(aggregatedInfo))
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            FilterAndSortPlacesUseCase(dispatcher)
        )
        viewModel.initialize()
        val categoryId = category.category.id
        viewModel.filterByCategory(categoryId)
        val categoryFilter = FilterOption.CategoryFilter(categoryId)
        advanceUntilIdle()
        assertTrue(viewModel.uiStateStream.value.appliedFilters.contains(categoryFilter))

    }

    @Test
    fun `filter by category should update filtered places`() = runTest {
        val validFilterPlace = Mocker.getFakePlace().copy(categories = listOf(Mocker.getFakeCategory().copy(id = "@filtered")))
        val notValidFilterPlace = Mocker.getFakePlace()
        val places = listOf(validFilterPlace,notValidFilterPlace)
        val filterAndSortPlacesUseCase = FilterAndSortPlacesUseCase(dispatcher)
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates = getLocationAvailabilityUpdates,
            getPlacesUseCase = getPlacesUseCase,
            filterAndSortPlacesUseCase = filterAndSortPlacesUseCase
        )
        viewModel._uiStateStream.emit(initialUIState.copy(places = places))
        viewModel.filterByCategory("@filtered")
        assertEquals(/* expected = */ listOf(validFilterPlace), /* actual = */ viewModel.uiStateStream.value.filteredPlaces)
    }

    //filter by verified should update filtered places and applied filters
    @Test
    fun `filter by verified should update filtered places and applied filters`() = runTest {
        val validFilterPlace = Mocker.getFakePlace().copy(verified = true)
        val notValidFilterPlace = Mocker.getFakePlace().copy(verified = false)
        val places = listOf(validFilterPlace, notValidFilterPlace)
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )

        viewModel._uiStateStream.emit(initialUIState.copy(places = places))

        viewModel.filterByVerified()

        val state = viewModel.uiStateStream.first()
        assertEquals(listOf(validFilterPlace), state.filteredPlaces)
        assertTrue(state.appliedFilters.contains(FilterOption.Verified))
    }

    @Test
    fun `filter by open now should update filtered places and applied filters`() = runTest {
        val validFilterPlace = Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_OPEN)
        val notValidFilterPlace = Mocker.getFakePlace().copy(openStatus = ClosedBucket.VERY_LIKELY_CLOSED)
        val places = listOf(validFilterPlace, notValidFilterPlace)
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )

        viewModel._uiStateStream.emit(initialUIState.copy(places = places))

        viewModel.filterByOpenNow()

        val state = viewModel.uiStateStream.first()
        assertEquals(listOf(validFilterPlace), state.filteredPlaces)
        assertTrue(state.appliedFilters.contains(FilterOption.OpenNow))
    }

    @Test
    fun `toggle sort by should update sorted places`() = runTest {
        val cheapPlace = Mocker.getFakePlace().copy(price = PriceBucket.CHEAP)
        val moderatePlace = Mocker.getFakePlace().copy(price = PriceBucket.MODERATE)
        val expensivePlace = Mocker.getFakePlace().copy(price = PriceBucket.EXPENSIVE)
        val veryExpensivePlace = Mocker.getFakePlace().copy(price = PriceBucket.VERY_EXPENSIVE)
        val places = listOf(expensivePlace, cheapPlace,veryExpensivePlace, moderatePlace)
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )
        val statesList = mutableListOf<PlaceUIState>()
        val job = launch(dispatcher) {
            viewModel.uiStateStream.toList(statesList)
        }
        viewModel._uiStateStream.emit(initialUIState.copy(places = places, filteredPlaces = places))
        viewModel.toggleSortBy(SortOption.PRICE)
        advanceUntilIdle()
        val expectedPlaces = listOf(cheapPlace, moderatePlace, expensivePlace, veryExpensivePlace)
        assertEquals(expectedPlaces, statesList.last().filteredPlaces)
        assertEquals(SortOption.PRICE, statesList.last().sortBy)
        job.cancel()

    }

    @Test
    fun `fetch places with error should update state with error`() = runTest {
        whenever(getLocationAvailabilityUpdates()).thenReturn(flowOf(LocationAvailability.Available))
        whenever(getPlacesUseCase()).thenReturn(flow { throw Exception("Error") })
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )

        viewModel.fetchPlaces()

        val state = viewModel.uiStateStream.first()
        assertTrue(state.showError)
    }

    @Test
    fun `fetch places should transition from loading to not loading`() = runTest(dispatcher) {
        val places = listOf(Mocker.getFakePlace())
        val categories = listOf(Mocker.getFakeCategory())
        val aggregatedInfo =
            PlacesWithAggregatedInfo(places, categories.map { CategoryToCount(it, 1) }, 1)

        whenever(getLocationAvailabilityUpdates()).thenReturn(flow {
            emit(LocationAvailability.Available)
        })
        whenever(getPlacesUseCase()).thenReturn(flow {
            //kotlinx.coroutines.delay(1000)
            emit(aggregatedInfo)
        })
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )
        val expectedStates = mutableListOf<PlaceUIState>()
        val job = launch {
            viewModel.uiStateStream.toList(expectedStates)  // Collect state changes
        }

        viewModel.fetchPlaces()
        advanceUntilIdle()
        assertEquals(3,expectedStates.size) //[0]initial state, [1]loading state, [2]success state
        assertTrue(expectedStates[1].isLoading)
        assertEquals(emptyList<Place>(),expectedStates[1].places)
        assertEquals(false,expectedStates[2].isLoading)
        assertEquals(places, expectedStates[2].places)
        job.cancel()
    }

    @Test
    fun `toggle category twice should remove the filter`() = runTest {
        val category = CategoryToCount(Mocker.getFakeCategory(), 1)
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )
        val categoryId = category.category.id
        viewModel.filterByCategory(categoryId)
        assertTrue(viewModel.uiStateStream.value.appliedFilters.contains(FilterOption.CategoryFilter(categoryId)))
        viewModel.filterByCategory(categoryId)
        assertFalse(viewModel.uiStateStream.value.appliedFilters.contains(FilterOption.CategoryFilter(categoryId)))
    }

    @Test
    fun `toggle different categories should append categories to appliedFilters`() = runTest {
        val category1 = CategoryToCount(Mocker.getFakeCategory().copy(id = "1"), 1)
        val category2 = CategoryToCount(Mocker.getFakeCategory().copy(id = "2"), 1)
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )
        val categoryId1 = category1.category.id
        val categoryId2 = category2.category.id
        viewModel.filterByCategory(categoryId1)
        assertTrue(viewModel.uiStateStream.value.appliedFilters.contains(FilterOption.CategoryFilter(categoryId1)))
        viewModel.filterByCategory(categoryId2)
        assertTrue(viewModel.uiStateStream.value.appliedFilters.contains(FilterOption.CategoryFilter(categoryId1)))
        assertTrue(viewModel.uiStateStream.value.appliedFilters.contains(FilterOption.CategoryFilter(categoryId2)))
    }

    @Test
    fun `toggle sort by twice should remove the filter`() = runTest {
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )
        viewModel.toggleSortBy(SortOption.PRICE)
        assertEquals(SortOption.PRICE, viewModel.uiStateStream.value.sortBy)
        viewModel.toggleSortBy(SortOption.PRICE)
        assertNull(viewModel.uiStateStream.value.sortBy)
    }

    @Test
    fun `toggle different sort should replace the old sort`() = runTest {
        val viewModel = PlacesViewModel(
            getLocationAvailabilityUpdates,
            getPlacesUseCase,
            filterAndSortPlacesUseCase
        )
        viewModel.toggleSortBy(SortOption.PRICE)
        assertEquals(SortOption.PRICE, viewModel.uiStateStream.value.sortBy)
        viewModel.toggleSortBy(SortOption.DISTANCE)
        assertEquals(SortOption.DISTANCE, viewModel.uiStateStream.value.sortBy)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    val initialUIState = PlaceUIState(
        places = emptyList(),
        isLoading = false,
        showError = false,
        isRefreshing = false,
        isInitialized = false,
        sortBy = null,
        categories = emptyList(),
        filteredPlaces = emptyList(),
        appliedFilters = emptyList()
    )
}