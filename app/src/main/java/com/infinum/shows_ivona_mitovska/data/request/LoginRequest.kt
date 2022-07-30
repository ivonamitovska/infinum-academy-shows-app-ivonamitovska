package com.infinum.shows_ivona_mitovska.data.request

import com.infinum.shows_ivona_mitovska.data.response.ShowsResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)
