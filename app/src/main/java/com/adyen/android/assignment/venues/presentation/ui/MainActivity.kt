package com.adyen.android.assignment.venues.presentation.ui

import VenueDiscoveryTheme
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adyen.android.assignment.core.miscs.Action
import com.adyen.android.assignment.core.miscs.ConsumableCommand
import com.adyen.android.assignment.location.LocationPermissionHandler
import com.adyen.android.assignment.location.presentaiton.GPSIsOffDialog
import com.adyen.android.assignment.location.presentaiton.LocationPermissionRationalDialog
import com.adyen.android.assignment.venues.presentation.MainViewModel
import com.adyen.android.assignment.venues.presentation.PermissionDenialStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var locationPermissionHandler: LocationPermissionHandler
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!locationPermissionHandler.isLocationPermissionGranted(this)) {
            requestLocationPermission()
        } else{
            mainViewModel.onPermissionGranted()
        }
        setContent {
            VenueDiscoveryTheme {
                VenueApp(
                    openSettings = ::goToAppSettings,
                    requestPermission = locationPermissionHandler::launchPermissionRequest,
                    openLocationSettings = ::goToLocationSettings,
                )
            }
        }
    }

    private fun requestLocationPermission() {
        locationPermissionHandler.requestPermission(this, onPermissionGranted = {
            mainViewModel.onPermissionGranted()
        }, onPermissionDenied = {
            mainViewModel.onPermissionDenied()
        }, onOpenLocationSettingsDialog = {
            Log.d("MainActivity", "onOpenLocationSettingsDialog")
            mainViewModel.onPermissionNeedsRational()
        })
    }

    private fun goToLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun goToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .apply {
                data = Uri.parse("package:${packageName}")
            }
        startActivity(intent)
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenueApp(
    openSettings: Action,
    requestPermission: Action,
    openLocationSettings: Action,
    mainViewModel: MainViewModel = viewModel()
) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    VenueDiscoveryTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Nearby Venues") })
            }
        ) { innerPadding ->
            PlacesScreen(Modifier.padding(innerPadding))
            RenderOnConsume(uiState.showPermissionNeeded) { status->
                //on consume
                LocationPermissionRationalDialog(
                    onDismiss = { },
                    onAllowClicked = if(status == PermissionDenialStatus.DeniedForever) openSettings else requestPermission,
                    canRequestPermission = status != PermissionDenialStatus.DeniedForever
                )
            }
            RenderOnConsume(uiState.showLocationMightBeOff){
                //on consume
                GPSIsOffDialog(
                    onDismiss = { },
                    onAllowClicked = openLocationSettings,
                )
            }
        }
    }
}

@Composable
fun <T> RenderOnConsume(command:ConsumableCommand<T>?, renderer: @Composable (T) -> Unit) {
    command?.consume()?.let { renderer(it) }
}
