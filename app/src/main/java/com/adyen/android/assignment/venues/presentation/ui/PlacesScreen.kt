package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.Place
import com.adyen.android.assignment.venues.presentation.PlacesViewModel

// PlacesScreen.kt
@Composable
fun PlacesScreen(modifier: Modifier,viewModel: PlacesViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyVerticalGrid(columns = GridCells.Adaptive(180.dp),modifier = modifier) {
        items(uiState.places) { place ->
            PlaceItem(place)
        }
    }
}

@Composable
fun PlaceItem(place: Place) {
    Card(modifier = Modifier.padding(8.dp).height(320.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(horizontal = 2.dp)) {
            //image of 64x64 for the place
            AsyncImage(
                model = place.photos.firstOrNull()?.small,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.size(180.dp)
            )
            Text(text = place.name, fontWeight = FontWeight.Bold,fontSize = 14.sp,lineHeight = 16.sp)
            Text(text = place.location?.address ?: "",fontSize = 12.sp,lineHeight = 14.sp)
            //categories section contains a list of categories of image and name
            CategoriesSection(place.categories)
        }
    }
}

//categories section contains a list of categories of image and name
@Composable
fun CategoriesSection(categories: List<Category>) {
    categories.take(1).forEach {
        CategoryItem(it)
    }
}

@Composable
fun CategoryItem(category: Category) {
    Row {
        AsyncImage(category.icons.small, contentDescription = null)
        Text(text = category.name, fontSize = 11.sp, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.secondary)
    }
}
