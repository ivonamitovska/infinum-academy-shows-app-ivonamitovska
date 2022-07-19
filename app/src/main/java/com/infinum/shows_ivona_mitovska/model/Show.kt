package com.infinum.shows_ivona_mitovska
import androidx.annotation.DrawableRes

data class Show(
    val name:String,
    @DrawableRes val imageResId: Int,
    val info:String
)
