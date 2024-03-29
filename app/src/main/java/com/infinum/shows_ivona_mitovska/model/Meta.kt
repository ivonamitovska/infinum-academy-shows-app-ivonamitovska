package com.infinum.shows_ivona_mitovska.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    @SerialName("pagination") val paggination:Pagination
)

@Serializable
data class Pagination (
@SerialName("count") val count:Int,
    @SerialName("page") val page:Int,
    @SerialName("items") val items:Int,
    @SerialName("pages") val pages:Int
)
