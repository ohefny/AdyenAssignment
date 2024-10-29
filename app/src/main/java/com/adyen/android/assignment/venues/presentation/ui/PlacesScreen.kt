package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.Place
import com.adyen.android.assignment.venues.presentation.PlacesViewModel

// PlacesScreen.kt
@Composable
fun PlacesScreen(modifier: Modifier, viewModel: PlacesViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = modifier) {
        items(uiState) { place ->
            PlaceItem(place)
        }
    }
}

@Composable
fun PlaceItem(place: Place) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column {
            //image of 64x64 for the place
            AsyncImage(
                model = place.photos.firstOrNull()?.small,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.padding(8.dp).size(132.dp)
            )
            Text(text = place.name, fontWeight = FontWeight.Bold)
            Text(text = place.location?.address ?: "")
            //categories section contains a list of categories of image and name
            CategoriesSection(place.categories)
        }
    }
}

//categories section contains a list of categories of image and name
@Composable
fun CategoriesSection(categories: List<Category>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories){ category ->
            CategoryItem(category)
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Row {
        AsyncImage(category.icons.small, contentDescription = null)
        Text(text = category.name, fontWeight = FontWeight.Bold)
    }
}
