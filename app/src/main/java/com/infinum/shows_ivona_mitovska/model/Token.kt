package com.infinum.shows_ivona_mitovska.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @SerialName("access-token") val accessToken: String,
    @SerialName("client") val client: String,
    @SerialName("token-type") val tokenType: String,
    @SerialName("expiry") val expiry: String,
    @SerialName("uid") val uid: String,
)
