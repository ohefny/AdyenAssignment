package com.adyen.android.assignment.venues.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adyen.android.assignment.venues.presentation.PlacesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adyen.android.assignment.venues.data.api.model.Place

// PlacesScreen.kt
@Composable
fun PlacesScreen(modifier: Modifier,viewModel: PlacesViewModel = viewModel()) {
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
            //Image(painter = rememberVectorPainter(place.name), contentDescription = null)
            Text(text = place.name, fontWeight = FontWeight.Bold)
            Text(text = place.name)
        }
    }
}
