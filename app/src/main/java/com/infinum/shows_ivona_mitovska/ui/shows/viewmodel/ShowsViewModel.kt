package com.infinum.shows_ivona_mitovska.ui.shows.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinum.shows_ivona_mitovska.data.response.ShowsResponse
import com.infinum.shows_ivona_mitovska.data.response.TopRatedResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.GenericResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.model.Show
import com.infinum.shows_ivona_mitovska.model.Token
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowsViewModel : ViewModel() {

    private val _showList = MutableLiveData<GenericResponse<List<Show>?>>()
    val showList: LiveData<GenericResponse<List<Show>?>>
        get() = _showList

    private val _showListTopRated = MutableLiveData<GenericResponse<List<Show>?>>()
    val showListTopRated: LiveData<GenericResponse<List<Show>?>>
        get() = _showListTopRated

    fun initShows(token: Token) {
        ApiModule.retrofit.getShows(token.accessToken, token.client, token.tokenType, token.expiry, token.uid)
            .enqueue(object : Callback<ShowsResponse> {
                override fun onResponse(call: Call<ShowsResponse>, response: Response<ShowsResponse>) {
                    if (response.isSuccessful) {
                        _showList.value = GenericResponse(response.body()?.shows, null, ResponseStatus.SUCCESS)
                    }else {
                        _showList.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                    }
                }

                override fun onFailure(call: Call<ShowsResponse>, t: Throwable) {
                    _showList.value = GenericResponse(null, t.localizedMessage, ResponseStatus.FAILURE)
                }

            })
    }

    fun topRated(token: Token){
        ApiModule.retrofit.getTopRated(token.accessToken, token.client, token.tokenType, token.expiry, token.uid)
            .enqueue(object : Callback<TopRatedResponse> {
                override fun onResponse(call: Call<TopRatedResponse>, response: Response<TopRatedResponse>) {
                    if (response.isSuccessful) {
                        _showListTopRated.value = GenericResponse(response.body()!!.shows,null,ResponseStatus.SUCCESS)
                    } else {
                        _showListTopRated.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                    }
                }

                override fun onFailure(call: Call<TopRatedResponse>, t: Throwable) {

                    _showListTopRated.value = GenericResponse(null, t.localizedMessage, ResponseStatus.FAILURE)
                }

            })
    }


}