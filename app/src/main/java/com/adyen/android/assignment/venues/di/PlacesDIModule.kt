package com.adyen.android.assignment.venues.di

import com.adyen.android.assignment.venues.data.DefaultPlacesRepository
import com.adyen.android.assignment.venues.data.PlacesRepository
import com.adyen.android.assignment.venues.data.api.PlacesService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
abstract class PlacesDIModule {

    @Binds
    @ViewModelScoped
    abstract fun bindPlacesRepository(repository: DefaultPlacesRepository): PlacesRepository


    companion object {
        @Provides
        @ViewModelScoped
        fun providePlacesService(retrofit: Retrofit): PlacesService {
            return retrofit.create(PlacesService::class.java)
        }
    }

}