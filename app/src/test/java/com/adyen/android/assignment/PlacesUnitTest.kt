package com.adyen.android.assignment

import com.adyen.android.assignment.venues.data.api.PlacesService
import com.adyen.android.assignment.venues.data.api.VenueRecommendationsQueryBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

class PlacesUnitTest {
    @Test
    fun testResponseCode() {
        val query = VenueRecommendationsQueryBuilder()
            .setLatitudeLongitude(52.376510, 4.905890)
            .build()
        runBlocking {
            val result = runCatching {
                PlacesService.instance.getVenueRecommendations(query)
            }.getOrNull()
            assertNotNull(result)
        }
    }
}
