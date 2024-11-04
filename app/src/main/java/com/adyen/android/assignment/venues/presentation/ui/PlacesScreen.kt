package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adyen.android.assignment.R
import com.adyen.android.assignment.venues.domain.FilterOption
import com.adyen.android.assignment.venues.domain.Place
import com.adyen.android.assignment.venues.presentation.PlacesViewModel
import com.adyen.android.assignment.venues.presentation.model.PlaceUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(modifier: Modifier, viewModel: PlacesViewModel = viewModel()) {
    val uiState by viewModel.uiStateStream.collectAsStateWithLifecycle()
    val isVerifiedChecked by remember(uiState.appliedFilters) {
        mutableStateOf(uiState.appliedFilters.any { it is FilterOption.Verified })
    }
    val isOpenNowChecked by remember(uiState.appliedFilters) {
        mutableStateOf(uiState.appliedFilters.any { it is FilterOption.OpenNow })
    }
    var sortToggle by rememberSaveable {  mutableStateOf(false) }
    var filterToggle by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel) {
        viewModel.initialize()
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchPlaces() }) {
        if (uiState.isLoading) {
            LoadingScreen()
        } else if (uiState.showError && !uiState.isInitialized) {
            ErrorScreen(onRetry = { viewModel.fetchPlaces() })
        } else {
            Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                // Category filter at the top
                CategoryFilter(
                    // Assume uiState contains the list of categories
                    onCategorySelected = { selectedCategory ->
                        viewModel.filterByCategory(selectedCategory.category.id)
                    },
                    categories = uiState.categories
                )
                if(filterToggle)
                    FiltersSection(
                        openNowChecked = isOpenNowChecked,
                        onOpenNowCheckedChange = { viewModel.filterByOpenNow() },
                        verifiedChecked = isVerifiedChecked,
                        onVerifiedCheckedChange = { viewModel.filterByVerified() }
                    )
                if (sortToggle)
                    SortOptions(uiState.sortBy) { viewModel.toggleSortBy(it) }

                ResultsTitle(
                    placeCount = uiState.filteredPlaces.size,
                    isSortOn = sortToggle,
                    isFilterOn = filterToggle,
                    onSortToggled = { sortToggle = !sortToggle },
                    onFilterToggled = { filterToggle = !filterToggle })
                PlacesResults(uiState)
            }
        }
    }

}

@Composable
private fun PlacesResults(uiState: PlaceUIState) {

    val listState = rememberLazyGridState()
    LaunchedEffect(uiState.appliedFilters.size, uiState.sortBy) {
        listState.animateScrollToItem(0) // Scroll to the first item
    }
    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Adaptive(180.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(uiState.filteredPlaces, key = { it.id }) { place ->
            PlaceItem(place)
        }
    }
}


@Composable
fun ResultsTitle(
    placeCount: Int,
    isSortOn: Boolean,
    isFilterOn: Boolean,
    onSortToggled: () -> Unit,
    onFilterToggled: () -> Unit
) {
    val sortColor =
        if (isSortOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val filterColor =
        if (isFilterOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Results",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_sort_24),
            contentDescription = "Sort Icon",
            tint = sortColor,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onSortToggled()
                }
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter_24),
            contentDescription = "Filter Icon",
            tint = filterColor,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onFilterToggled()
                }
        )
        Text(
            text = "$placeCount places found",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}


@Composable
private fun LoadingScreen() {
    PlacesGridPlaceHolder()
}

@Preview(showBackground = true)
@Composable
fun PlacesGridPlaceHolder() {
    LazyVerticalGrid(
        contentPadding = PaddingValues(16.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),

        ) {
        items(16) {
            PlaceItemPlaceHolder()
        }
    }
}

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //add icon
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_wifi_error_24),
            contentDescription = "Error Icon",
            modifier = Modifier.size(100.dp)
        )
        Text(
            "We couldn\'t complete your request.\nPlease check your internet connection or try again.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}



