package com.infinum.shows_ivona_mitovska.ui.shows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinum.shows_ivona_mitovska.ShowRepository
import com.infinum.shows_ivona_mitovska.model.Show

class ShowsViewModel : ViewModel() {

    private val _showList = MutableLiveData<List<Show>>()
    val showList: LiveData<List<Show>>
        get() = _showList

    init {
        _showList.value = ShowRepository.getShows()
    }


}