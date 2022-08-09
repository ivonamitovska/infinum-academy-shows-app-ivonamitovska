package com.infinum.shows_ivona_mitovska.ui.show_details.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.infinum.shows_ivona_mitovska.data.request.CreateReviewRequest
import com.infinum.shows_ivona_mitovska.data.response.CreateReviewResponse
import com.infinum.shows_ivona_mitovska.data.response.ReviewsListResponse
import com.infinum.shows_ivona_mitovska.data.response.ShowDetailsResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.GenericResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.database.ShowApplication
import com.infinum.shows_ivona_mitovska.database.ShowsDatabase
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.model.Show
import com.infinum.shows_ivona_mitovska.model.Token
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import java.text.DecimalFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowDetailsViewModel(private val database: ShowsDatabase, app: Application) : AndroidViewModel(app) {
    private val _show = MutableLiveData<GenericResponse<Show?>>()
    val show: LiveData<GenericResponse<Show?>>
        get() = _show

    private val _reviews = MutableLiveData<GenericResponse<List<Review>?>>()
    val reviews: LiveData<GenericResponse<List<Review>?>>
        get() = _reviews

    fun initReviews(showId: String, token: Token) {
        if (hasInternetConnection()) {
            ApiModule.retrofit.getReviews(token.accessToken, token.client, token.tokenType, token.expiry, token.uid, showId)
                .enqueue(object : Callback<ReviewsListResponse> {
                    override fun onResponse(call: Call<ReviewsListResponse>, response: Response<ReviewsListResponse>) {
                        if (response.isSuccessful) {
                            _reviews.value = GenericResponse(response.body()!!.reviews, null, ResponseStatus.SUCCESS)
                            viewModelScope.launch(Dispatchers.IO) {
                                database.reviewDao().insertAllReviews(response.body()!!.reviews)
                            }

                        } else {
                            _reviews.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                        }
                    }

                    override fun onFailure(call: Call<ReviewsListResponse>, t: Throwable) {
                        _reviews.value = GenericResponse(null, t.localizedMessage, ResponseStatus.FAILURE)
                    }

                })
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val reviews = database.reviewDao().getAllReviews(showId)
                withContext(Dispatchers.Main) {
                    reviews.observeForever {
                        _reviews.value = GenericResponse(it, null, ResponseStatus.SUCCESS)
                    }
                }
            }

        }

    }

    fun initShow(id: String, token: Token) {
        if (hasInternetConnection()) {
            ApiModule.retrofit.getShowDetails(token.accessToken, token.client, token.tokenType, token.expiry, token.uid, id)
                .enqueue(object : Callback<ShowDetailsResponse> {
                    override fun onResponse(call: Call<ShowDetailsResponse>, response: Response<ShowDetailsResponse>) {
                        if (response.isSuccessful) {
                            _show.value = GenericResponse(response.body()!!.show, null, ResponseStatus.SUCCESS)
                            viewModelScope.launch(Dispatchers.IO) {
                                database.showDao().getShow(id)
                            }

                        } else {
                            _show.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                        }
                    }

                    override fun onFailure(call: Call<ShowDetailsResponse>, t: Throwable) {
                        _show.value = GenericResponse(null, t.localizedMessage, ResponseStatus.FAILURE)
                    }

                })
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val show = database.showDao().getShow(id)
                withContext(Dispatchers.Main) {
                    show.observeForever {
                        _show.value = GenericResponse(it, null, ResponseStatus.SUCCESS)
                    }
                }
            }

        }

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
                    if (response.isSuccessful) {
                        val list = _reviews.value!!.data as MutableList
                        list.add(response.body()!!.review)
                        _reviews.value = GenericResponse(list, null, ResponseStatus.SUCCESS)
                        viewModelScope.launch(Dispatchers.IO) { database.reviewDao().addReview(response.body()!!.review) }

                    } else {
                        _reviews.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                    }
                }

                override fun onFailure(call: Call<CreateReviewResponse>, t: Throwable) {
                    _reviews.value = GenericResponse(reviews.value?.data, t.localizedMessage, ResponseStatus.FAILURE)
                }

            })

    }

    fun getAverageReviewRating(): String {
        var average = 0.0
        reviews.value?.data?.forEach { average += it.rating }
        return DecimalFormat("#.##").format(average / reviews.value?.data?.size!!)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ShowApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false

        }
    }

}