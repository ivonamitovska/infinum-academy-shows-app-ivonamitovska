package com.infinum.shows_ivona_mitovska.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "show")
@Serializable
data class Show(
    @SerialName ("id") @ColumnInfo (name = "id") @PrimaryKey val id: String,
    @SerialName("average_rating") @ColumnInfo(name = "averageRating") val averageRating: Double?,
    @SerialName("description") @ColumnInfo(name = "description") val description: String?,
    @SerialName("image_url") @ColumnInfo(name = "image")  val imageUrl: String,
    @SerialName("no_of_reviews") @ColumnInfo(name = "numberReviews") val noOfReviews: Int,
    @SerialName("title") @ColumnInfo(name = "title") val title: String

) : java.io.Serializable



