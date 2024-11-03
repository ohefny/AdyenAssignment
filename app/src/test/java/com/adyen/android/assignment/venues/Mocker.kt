package com.adyen.android.assignment.venues

import com.adyen.android.assignment.venues.data.api.model.CategoryResponse
import com.adyen.android.assignment.venues.data.api.model.CoordinateResponse
import com.adyen.android.assignment.venues.data.api.model.GeoCodeResponse
import com.adyen.android.assignment.venues.data.api.model.IconResponse
import com.adyen.android.assignment.venues.data.api.model.LocationResponse
import com.adyen.android.assignment.venues.data.api.model.PhotoResponse
import com.adyen.android.assignment.venues.data.api.model.PlaceResponse
import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.ClosedBucket
import com.adyen.android.assignment.venues.domain.IconBucket
import com.adyen.android.assignment.venues.domain.Location
import com.adyen.android.assignment.venues.domain.PhotoBucket
import com.adyen.android.assignment.venues.domain.Place
import java.util.UUID

object Mocker {

    fun getFakePlaceResponse(): PlaceResponse {
        return PlaceResponse(
            id = UUID.randomUUID().toString(),
            name = "Fake Place",
            location = LocationResponse(
                address = "Fake Address",
                country = "Fake Country",
                locality = "Fake Locality",
                postcode = "Fake Postcode"
            ),
            geocode = GeoCodeResponse(
                main = CoordinateResponse(0.0, 0.0)
            ),
            categories = listOf(
                CategoryResponse(
                    id = "1",
                    name = "Fake Category",
                    iconResponse = IconResponse(
                        prefix = "Fake Prefix",
                        suffix = "Fake Suffix"
                    )
                )
            ),
            distance = 0,
            timezone = "Fake Timezone",
            photos = listOf(
                PhotoResponse(
                    prefix = "Fake Prefix",
                    suffix = "Fake Suffix",
                    width = 0,
                    height = 0,
                    id = "1",
                    createdAt = ""
                )
            ),
            closedBucket = ClosedBucket.LIKELY_OPEN.name,
            verified = true,
            rating = 9.0,
            price = null,
        )
    }

    fun getFakeCategory(): Category {
        return Category(
            id = "1",
            name = "Fake Category",
            icons = IconBucket(
                small = "Small",
                tiny = "Tiny"
            )
        )
    }

    fun getFakePlace(): Place {
        return Place(
            id = UUID.randomUUID().toString(),
            name = "Fake Place",
            location = Location(
                address = "Fake Address",
                lat = 0.0,
                lng = 0.0
            ),
            categories = listOf(
                Category(
                    id = "1",
                    name = "Fake Category",
                    icons = IconBucket(
                        small = "Small",
                        tiny = "Tiny"
                    )
                )
            ),
            distance = 0,
            timezone = "Fake Timezone",
            photos = listOf(
                PhotoBucket(
                    small = "small Photo",
                    medium = "medium Photo",
                    large = "large Photo"
                )
            ),
            openStatus = ClosedBucket.LIKELY_OPEN,
            verified = true,
            rating = 9.0,
            price = null,
        )
    }
}