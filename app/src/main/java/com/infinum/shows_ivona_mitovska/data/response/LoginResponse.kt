package com.infinum.shows_ivona_mitovska.data.response


import com.infinum.shows_ivona_mitovska.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("user") val user: User

)
