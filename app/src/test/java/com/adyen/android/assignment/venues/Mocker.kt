package com.adyen.android.assignment.venues

import com.adyen.android.assignment.venues.domain.Category
import com.adyen.android.assignment.venues.domain.ClosedBucket
import com.adyen.android.assignment.venues.domain.IconBucket
import com.adyen.android.assignment.venues.domain.Location
import com.adyen.android.assignment.venues.domain.PhotoBucket
import com.adyen.android.assignment.venues.domain.Place
import java.util.UUID

object Mocker {

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
            openStatus = ClosedBucket.LIKELY_OPEN
        )
    }
}