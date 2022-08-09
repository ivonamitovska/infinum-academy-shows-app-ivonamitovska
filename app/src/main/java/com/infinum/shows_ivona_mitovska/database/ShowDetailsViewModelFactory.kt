package com.infinum.shows_ivona_mitovska.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.infinum.shows_ivona_mitovska.ui.show_details.viewmodel.ShowDetailsViewModel
import com.infinum.shows_ivona_mitovska.ui.shows.viewmodel.ShowsViewModel
import java.lang.IllegalArgumentException

class ShowDetailsViewModelFactory(val database: ShowsDatabase,val app:Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass.isAssignableFrom(ShowDetailsViewModel::class.java)){
            return ShowDetailsViewModel(database, app) as T
        }
        throw IllegalArgumentException("Sorry, we can work with non ShowDetailsViewModel classes")

    }
}