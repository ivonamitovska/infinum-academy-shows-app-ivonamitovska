package com.infinum.shows_ivona_mitovska.ui.login.viemodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinum.shows_ivona_mitovska.data.request.LoginRequest
import com.infinum.shows_ivona_mitovska.data.response.LoginResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.GenericResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.model.Token
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val loginResultLiveData: MutableLiveData<GenericResponse<Token?>> by lazy { MutableLiveData<GenericResponse<Token?>>() }

    fun getLoginResultLiveData(): LiveData<GenericResponse<Token?>> {
        return loginResultLiveData
    }

    fun onLoginButtonClicked(email: String, password: String) {
        val loginRequest = LoginRequest(
            email = email,
            password = password
        )
        ApiModule.retrofit.signIn(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.code() == 201) {
                        val accessToken = response.headers()["access-token"]
                        val client = response.headers()["client"]
                        val tokenType = response.headers()["token-type"]
                        val expiry = response.headers()["expiry"]
                        val uid = response.headers()["uid"]
                        val token = Token(accessToken!!, client!!, tokenType!!, expiry!!, uid!!)
                        loginResultLiveData.value = GenericResponse(token, null, ResponseStatus.SUCCESS)
                    } else {
                        loginResultLiveData.value = GenericResponse(null, "UNAUTHORIZED", ResponseStatus.FAILURE)
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResultLiveData.value = GenericResponse(null, "Something went wrong", ResponseStatus.FAILURE)
                }

            })

    }
}