package com.adyen.android.assignment.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.adyen.android.assignment.core.miscs.Action
import javax.inject.Inject

class LocationPermissionHandler @Inject constructor() {
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private var onPermissionGranted: Action? = null
    private var onPermissionDenied: Action? = null

    private fun registerPermissionResult(activity: ComponentActivity) {
        Log.d("LocationPermissionHandl", "registerPermissionResult() called with: activity initialized = ${::requestLocationPermissionLauncher.isInitialized}")
        if (::requestLocationPermissionLauncher.isInitialized) return
        requestLocationPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted)
                onPermissionGranted?.invoke()
            else
                onPermissionDenied?.invoke()
        }
    }

    fun requestPermission(
        activity: ComponentActivity,
        onPermissionGranted: Action,
        onPermissionDenied: Action,
        onOpenLocationSettingsDialog: Action,
    ) {
        this.onPermissionGranted = onPermissionGranted
        this.onPermissionDenied = onPermissionDenied
        if (isLocationPermissionGranted(activity)) {
            this.onPermissionGranted?.invoke()
            return
        }
        registerPermissionResult(activity)
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            onOpenLocationSettingsDialog()
        } else {
            launchPermissionRequest()
        }
    }

    fun launchPermissionRequest() {
        requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun isLocationPermissionGranted(activity: ComponentActivity): Boolean {
        return activity.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION).also {
            Log.d("LocationPermissionHandl", "isLocationPermissionGranted() = $it")
        }
    }

}


fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}
