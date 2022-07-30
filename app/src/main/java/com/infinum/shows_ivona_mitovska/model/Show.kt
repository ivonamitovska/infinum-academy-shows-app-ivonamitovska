package com.infinum.shows_ivona_mitovska.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Show(
    //
    //    val id: Int,
    //    val name: String,
    //    @DrawableRes val imageResId: Int,
    //    val info: String
    @SerialName("id") val id: String,
    @SerialName("average_rating") val averageRating: Double?,
    @SerialName("description") val description: String?,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("no_of_reviews") val noOfReviews: Int,
    @SerialName("title") val title: String

) : java.io.Serializable

