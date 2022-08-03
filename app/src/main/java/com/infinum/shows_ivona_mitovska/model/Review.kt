package com.infinum.shows_ivona_mitovska.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "review")
@Serializable
data class Review(
    @SerialName ("id") @ColumnInfo(name = "id") @PrimaryKey val id:String,
    @SerialName("comment") @ColumnInfo(name = "comment") val comment:String,
    @SerialName("rating") @ColumnInfo(name = "rating") val rating:Int,
    @SerialName("show_id") @ColumnInfo(name = "showId") val showId:Int,
    @SerialName("user") @ColumnInfo(name = "user") val user:User
)
