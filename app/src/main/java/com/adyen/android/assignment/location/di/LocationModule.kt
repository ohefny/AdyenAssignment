package com.adyen.android.assignment.location.di

import android.content.Context
import com.adyen.android.assignment.location.data.DefaultLocationRepository
import com.adyen.android.assignment.location.data.DefaultLocationProviderClient
import com.adyen.android.assignment.location.data.LocationProviderClient
import com.adyen.android.assignment.location.domain.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    abstract fun provideLocationRepository(fusedLocationProvider: DefaultLocationRepository): LocationRepository
    @Binds
    abstract fun bindLocationProviderClient(locationProviderClient: DefaultLocationProviderClient): LocationProviderClient

    companion object {
        @Provides
        fun provideFusedLocation(@ApplicationContext context: Context): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }
    }

}
