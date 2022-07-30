package com.infinum.shows_ivona_mitovska.data.request

import com.infinum.shows_ivona_mitovska.data.response.ShowsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewRequest(
    @SerialName("rating") val rating:Int,
    @SerialName("comment") val comment:String,
    @SerialName("show_id") val showId:Int
)
