package com.infinum.shows_ivona_mitovska.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("image_url") val imageUrl: String?
) {
    override fun toString(): String {
        return "$id $email $imageUrl"
    }
}
