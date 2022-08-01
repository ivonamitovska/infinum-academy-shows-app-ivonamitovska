package com.infinum.shows_ivona_mitovska.ui.register.viewmodel

import RegisterResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinum.shows_ivona_mitovska.data.request.RegisterRequest
import com.infinum.shows_ivona_mitovska.data.response.generic.GenericResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val registrationResultLiveData: MutableLiveData<GenericResponse<Boolean>> by lazy { MutableLiveData<GenericResponse<Boolean>>() }

    fun getRegistrationResultLiveData(): LiveData<GenericResponse<Boolean>> {
        return registrationResultLiveData
    }

    fun onRegisterButtonClicked(email: String, password: String) {
        val registerRequest = RegisterRequest(
            email = email,
            password = password,
            passwordConfirmation = password
        )
        ApiModule.retrofit.register(registerRequest)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if(response.code()==201){
                        registrationResultLiveData.value = GenericResponse(response.isSuccessful, "", ResponseStatus.SUCCESS)
                    }
                    else{
                        registrationResultLiveData.value = GenericResponse(false, "Email is not an email", ResponseStatus.FAILURE)
                    }

                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registrationResultLiveData.value = GenericResponse(false, t.localizedMessage, ResponseStatus.FAILURE)
                }
            })
    }
}