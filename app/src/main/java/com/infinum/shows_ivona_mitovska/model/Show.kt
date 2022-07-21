package com.infinum.shows_ivona_mitovska.model

import androidx.annotation.DrawableRes

data class Show(
    val id: Int,
    val name: String,
    @DrawableRes val imageResId: Int,
    val info: String
)
