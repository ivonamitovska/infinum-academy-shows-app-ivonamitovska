package com.infinum.shows_ivona_mitovska.ui.shows.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.infinum.shows_ivona_mitovska.data.response.ShowsResponse
import com.infinum.shows_ivona_mitovska.data.response.TopRatedResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.GenericResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.database.ShowApplication
import com.infinum.shows_ivona_mitovska.database.ShowsDatabase
import com.infinum.shows_ivona_mitovska.model.Show
import com.infinum.shows_ivona_mitovska.model.Token
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel(
    private val database: ShowsDatabase,
    app: Application
) : AndroidViewModel(app) {

    private val _showList = MutableLiveData<GenericResponse<List<Show>?>>()
    val showList: LiveData<GenericResponse<List<Show>?>>
        get() = _showList

    private val _showListTopRated = MutableLiveData<GenericResponse<List<Show>?>>()
    val showListTopRated: LiveData<GenericResponse<List<Show>?>>
        get() = _showListTopRated


    fun initShows(token: Token) {
        if (hasInternetConnection()) {
            ApiModule.retrofit.getShows(token.accessToken, token.client, token.tokenType, token.expiry, token.uid)
                .enqueue(object : Callback<ShowsResponse> {
                    override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                        if (response.isSuccessful) {
                            _showList.value = GenericResponse(response.body()?.shows, null, ResponseStatus.SUCCESS)
                            viewModelScope.launch(Dispatchers.IO) {
                                database.showDao().insertAllShows(response.body()!!.shows)
                            }
                        } else {
                            _showList.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                        }
                    }

                    override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                        _showList.value = GenericResponse(null, t.localizedMessage, ResponseStatus.FAILURE)
                    }

                })
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val shows = database.showDao().getAllShows()
                withContext(Dispatchers.Main) {
                    shows.observeForever {
                        _showList.value = GenericResponse(it, null, ResponseStatus.SUCCESS)
                    }
                }
            }
        }


    }

    fun topRated(token: Token) {
        ApiModule.retrofit.getTopRated(token.accessToken, token.client, token.tokenType, token.expiry, token.uid)
            .enqueue(object : Callback<TopRatedResponse> {
                override fun onResponse(call: Call<TopRatedResponse>, response: Response<TopRatedResponse>) {
                    if (response.isSuccessful) {
                        _showListTopRated.value = GenericResponse(response.body()!!.shows, null, ResponseStatus.SUCCESS)
                    } else {
                        _showListTopRated.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                    }
                }

                override fun onFailure(call: Call<TopRatedResponse>, t: Throwable) {

                    _showListTopRated.value = GenericResponse(null, t.localizedMessage, ResponseStatus.FAILURE)
                }

            })
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<ShowApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false

        }
    }


}