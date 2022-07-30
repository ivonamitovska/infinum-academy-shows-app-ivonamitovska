package com.infinum.shows_ivona_mitovska.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Show(
    val id: Int,
    val name: String,
    @DrawableRes val imageResId: Int,
    val info: String
) : Parcelable
