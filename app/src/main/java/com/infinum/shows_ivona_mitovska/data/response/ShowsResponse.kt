package com.infinum.shows_ivona_mitovska.data.response

import com.infinum.shows_ivona_mitovska.model.Meta
import com.infinum.shows_ivona_mitovska.model.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Show>,
    @SerialName("meta") val meta:Meta
)
