/*package com.adyen.android.assignment.venues.di

import android.content.Context
import androidx.activity.ComponentActivity
import com.adyen.android.assignment.location.LocationPermissionHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object PermissionModule {

    @Provides
    @ActivityScoped
    fun providePermissionHandler(@ActivityContext activity: Context): LocationPermissionHandler {
        return LocationPermissionHandler(activity)
    }
}*/