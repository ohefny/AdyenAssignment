package com.adyen.android.assignment.venues.data.api

class VenueRecommendationsQueryBuilder : PlacesQueryBuilder() {
    private var latitudeLongitude: String? = null
    private var fields: String = "fsq_id,photos,categories,closed_bucket,location,name,distance,geocodes,timezone,related_places"
    fun setLatitudeLongitude(latitude: Double, longitude: Double): VenueRecommendationsQueryBuilder {
        this.latitudeLongitude = "$latitude,$longitude"
        return this
    }

    fun setRequiredFields(vararg fields:String): VenueRecommendationsQueryBuilder {
        this.fields = fields.joinToString(",")
        return this
    }

    override fun putQueryParams(queryParams: MutableMap<String, String>) {
        latitudeLongitude?.apply { queryParams["ll"] = this }
        queryParams["fields"] = fields
    }
}
