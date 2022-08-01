package com.infinum.shows_ivona_mitovska.ui.show_details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinum.shows_ivona_mitovska.data.request.CreateReviewRequest
import com.infinum.shows_ivona_mitovska.data.response.CreateReviewResponse
import com.infinum.shows_ivona_mitovska.data.response.ReviewsListResponse
import com.infinum.shows_ivona_mitovska.data.response.ShowDetailsResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.GenericResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.model.Show
import com.infinum.shows_ivona_mitovska.model.Token
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import java.text.DecimalFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel : ViewModel() {
    private val _show = MutableLiveData<GenericResponse<Show?>>()
    val show: LiveData<GenericResponse<Show?>>
        get() = _show

    private val _reviews = MutableLiveData<GenericResponse<List<Review>?>>()
    val reviews: LiveData<GenericResponse<List<Review>?>>
        get() = _reviews

    fun initReviews(showId: String, token: Token) {
        ApiModule.retrofit.getReviews(token.accessToken, token.client, token.tokenType, token.expiry, token.uid, showId)
            .enqueue(object : Callback<ReviewsListResponse> {
                override fun onResponse(call: Call<ReviewsListResponse>, response: Response<ReviewsListResponse>) {
                    if (response.code() == 200) {
                        _reviews.value = GenericResponse(response.body()!!.reviews, null, ResponseStatus.SUCCESS)
                    } else if(response.code()==401){
                        _reviews.value = GenericResponse(null, "You need to sign in or sign up before continuing.", ResponseStatus.FAILURE)
                    }
                    else{
                        _reviews.value = GenericResponse(null, "Couldn't find Show with $showId=bad_id", ResponseStatus.FAILURE)
                    }
                }

                override fun onFailure(call: Call<ReviewsListResponse>, t: Throwable) {
                    _reviews.value = GenericResponse(null, "Something went wrong", ResponseStatus.FAILURE)
                }

            })
    }

    fun initShow(id: String, token: Token) {
        ApiModule.retrofit.getShowDetails(token.accessToken, token.client, token.tokenType, token.expiry, token.uid, id)
            .enqueue(object : Callback<ShowDetailsResponse> {
                override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                    if (response.code() == 200) {
                        _show.value = GenericResponse(response.body()!!.show, null, ResponseStatus.SUCCESS)
                    }else if(response.code()==401){
                        _show.value = GenericResponse(null, "You need to sign in or sign up before continuing.", ResponseStatus.FAILURE)
                    }
                    else{
                        _show.value = GenericResponse(null, "Couldn't find Show with $id=bad_id", ResponseStatus.FAILURE)
                    }
                }

                override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                    _show.value = GenericResponse(null, "Something went wrong", ResponseStatus.FAILURE)
                }

            })
    }

    fun addReview(comment: String, rating: Int, showId: Int, token: Token) {
        val reviewRequest = CreateReviewRequest(
            comment = comment,
            rating = rating,
            showId = showId
        )
        ApiModule.retrofit.createReview(token.accessToken, token.client, token.tokenType, token.expiry, token.uid, reviewRequest)
            .enqueue(object : Callback<CreateReviewResponse> {
                override fun onResponse(call: Call<CreateReviewResponse>, response: Response<CreateReviewResponse>) {
                    if (response.code() == 201) {
                        val list = _reviews.value?.data as MutableList
                        list.add(response.body()!!.review)
                        _reviews.value = GenericResponse(list, null, ResponseStatus.SUCCESS)
                    } else if(response.code()==401) {
                        _reviews.value = GenericResponse(null, "You need to sign in or sign up before continuing.", ResponseStatus.FAILURE)
                    }
                    else{
                        _reviews.value = GenericResponse(null, "Show must exist,Rating is not a number", ResponseStatus.FAILURE)
                    }
                }

                override fun onFailure(call: Call<CreateReviewResponse>, t: Throwable) {
                    _reviews.value = GenericResponse(reviews.value?.data, "Something went wrong", ResponseStatus.FAILURE)
                }

            })

    }

    fun getAverageReviewRating(): String {
        var average = 0.0
        reviews.value?.data?.forEach { average += it.rating }
        return DecimalFormat("#.##").format(average / reviews.value?.data?.size!!)
    }

}