package com.adyen.android.assignment.venues.domain

import java.util.Date

data class Place(
    val id: String,
    val categories: List<Category>,
    val distance: Int,
    val location: Location?,
    val name: String,
    val timezone: String,
    val photos: List<PhotoBucket>,
    val openStatus: ClosedBucket?,
    val verified: Boolean? = null,
    val rating: Double? = null,
    val dateClosed: Date? = null,
    val price: PriceBucket? = null
)

data class Location(
    val address: String,
    val lat: Double,
    val lng: Double
)
data class Category(
    val id: String,
    val name: String,
    val icons: IconBucket
)
data class IconBucket(
    val small: String,
    val tiny:String,
){
    companion object {
        fun empty() = IconBucket("", "")
    }
}

data class PhotoBucket(
    val small: String,
    val medium: String,
    val large: String,
)


enum class ClosedBucket(val id: String) {
    VERY_LIKELY_OPEN("VeryLikelyOpen"),
    LIKELY_OPEN("LikelyOpen"),
    UNSURE("Unsure"),
    LIKELY_CLOSED("LikelyClosed"),
    VERY_LIKELY_CLOSED("VeryLikelyClosed");

    companion object {
        fun fromString(value: String): ClosedBucket? {
            return entries.find { it.id.equals(value, ignoreCase = true) }
        }
    }
}

enum class PriceBucket(val id:Int) {
    CHEAP(1),
    MODERATE(2),
    EXPENSIVE(3),
    VERY_EXPENSIVE(4);

    companion object {
        fun fromNumericalValue(value: Int): PriceBucket {
            return entries.first { it.id == value }
        }
    }
}