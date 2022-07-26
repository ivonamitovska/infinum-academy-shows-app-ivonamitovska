package com.infinum.shows_ivona_mitovska.ui.show_details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinum.shows_ivona_mitovska.ReviewRepository
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.model.Show
import java.text.DecimalFormat

class ShowDetailsViewModel : ViewModel() {
    private val _show = MutableLiveData<Show>()
    val show: LiveData<Show>
        get() = _show

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>>
        get() = _reviews

    init {
        _reviews.value = ReviewRepository.getReviews()
    }

    fun initShow(selectedShow: Show) {
        _show.value = selectedShow
    }

    fun addReview(comment: String, username: String, rating: Int) {
        val list = _reviews.value!!.toMutableList()
        list.add(Review(comment, username, rating))
        _reviews.value = list
    }

    fun getAverageReviewRating(): String {
        var average = 0.0
        reviews.value?.forEach { average += it.review }
        return DecimalFormat("#.##").format(average / reviews.value?.size!!)
    }

}