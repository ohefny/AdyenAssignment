package com.adyen.android.assignment.venues.presentation.ui

import VenueDiscoveryTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VenueDiscoveryTheme {
                VenueApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueApp() {

    VenueDiscoveryTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Nearby Venues") })
            }
        ) { innerPadding ->
            PlacesScreen(Modifier.padding(innerPadding))
        }
    }
}

