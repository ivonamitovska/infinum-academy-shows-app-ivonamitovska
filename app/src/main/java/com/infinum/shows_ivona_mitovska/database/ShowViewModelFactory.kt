package com.infinum.shows_ivona_mitovska.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.infinum.shows_ivona_mitovska.ui.shows.viewmodel.ShowsViewModel
import java.lang.IllegalArgumentException

class ShowViewModelFactory(val database: ShowsDatabase,val app:Application):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass.isAssignableFrom(ShowsViewModel::class.java)){
            return ShowsViewModel(database,app) as T
        }
        throw IllegalArgumentException("Sorry, we can work with non ShowsViewModel classes")

    }
}