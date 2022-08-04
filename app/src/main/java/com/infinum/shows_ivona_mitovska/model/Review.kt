package com.infinum.shows_ivona_mitovska.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
//    val comment: String,
//    val name: String,
//    val review: Int
    @SerialName ("id") val id:String,
    @SerialName("comment") val comment:String,
    @SerialName("rating") val rating:Int,
    @SerialName("show_id") val showId:Int,
    @SerialName("user") val user:User
)
