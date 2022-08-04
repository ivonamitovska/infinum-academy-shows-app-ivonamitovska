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
import com.infinum.shows_ivona_mitovska.utils.Constants.ACCESS_TOKEN
import com.infinum.shows_ivona_mitovska.utils.Constants.CLIENT
import com.infinum.shows_ivona_mitovska.utils.Constants.EXPIRY
import com.infinum.shows_ivona_mitovska.utils.Constants.TOKEN_TYPE
import com.infinum.shows_ivona_mitovska.utils.Constants.UID
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
                    if (response.isSuccessful) {
                        val accessToken = response.headers()[ACCESS_TOKEN]
                        val client = response.headers()[CLIENT]
                        val tokenType = response.headers()[TOKEN_TYPE]
                        val expiry = response.headers()[EXPIRY]
                        val uid = response.headers()[UID]
                        val token = Token(accessToken!!, client!!, tokenType!!, expiry!!, uid!!)
                        loginResultLiveData.value = GenericResponse(token, null, ResponseStatus.SUCCESS)
                    } else {
                        loginResultLiveData.value = GenericResponse(null, response.errorBody().toString(), ResponseStatus.FAILURE)
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResultLiveData.value = GenericResponse(null, t.localizedMessage, ResponseStatus.FAILURE)
                }

            })

    }
}