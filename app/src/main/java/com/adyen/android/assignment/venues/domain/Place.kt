package com.adyen.android.assignment.venues.domain

data class Place(
    val id: String,
    val categories: List<Category>,
    val distance: Int,
    val location: Location?,
    val name: String,
    val timezone: String,
    val photos: List<PhotoBucket>,
    val openStatus: ClosedBucket?
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
)

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