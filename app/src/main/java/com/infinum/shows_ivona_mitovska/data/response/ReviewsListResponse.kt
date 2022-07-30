package com.infinum.shows_ivona_mitovska.data.response


import com.infinum.shows_ivona_mitovska.model.Meta
import com.infinum.shows_ivona_mitovska.model.Review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsListResponse(
    @SerialName("reviews") val reviews:List<Review>,
    @SerialName("meta") val meta:Meta


)
