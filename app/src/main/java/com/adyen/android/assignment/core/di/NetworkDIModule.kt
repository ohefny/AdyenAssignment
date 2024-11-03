package com.adyen.android.assignment.core.di
import com.adyen.android.assignment.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDIModule {
    @Provides
    @Singleton
    fun provideMoshiJson(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesRetrofitV2(
        networkJson: MoshiConverterFactory
    ): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
            .addConverterFactory(networkJson)
            .client(okHttpClient)
            .build()
    }
}