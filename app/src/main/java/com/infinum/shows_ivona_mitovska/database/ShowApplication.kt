package com.infinum.shows_ivona_mitovska.database

import android.app.Application

class ShowApplication:Application() {
    val database by lazy {
        ShowsDatabase.getDatabase(this)
    }

}