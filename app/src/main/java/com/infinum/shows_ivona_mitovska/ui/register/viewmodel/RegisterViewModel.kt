package com.infinum.shows_ivona_mitovska.ui.register.viewmodel

import RegisterResponse
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.infinum.shows_ivona_mitovska.data.request.RegisterRequest
import com.infinum.shows_ivona_mitovska.data.response.generic.GenericResponse
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import com.infinum.shows_ivona_mitovska.ui.register.RegisterValidity
import com.infinum.shows_ivona_mitovska.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val registerValidity = RegisterValidity()
    private val registrationResultLiveData: MutableLiveData<GenericResponse<Boolean>> by lazy { MutableLiveData<GenericResponse<Boolean>>() }

    fun getRegistrationResultLiveData(): LiveData<GenericResponse<Boolean>> {
        return registrationResultLiveData
    }

    private val emailRegisterLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    fun getEmailRegisterLiveData(): LiveData<String> {
        return emailRegisterLiveData
    }

    private val passwordRegisterLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    fun getPasswordRegisterLiveData(): LiveData<String> {
        return passwordRegisterLiveData
    }

    private val passwordRepeatRegisterLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    fun getPasswordRepeatRegisterLiveData(): LiveData<String> {
        return passwordRepeatRegisterLiveData
    }

    private val checkValidityRegisterLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    fun getRegisterValidityLiveData(): LiveData<Boolean> {
        return checkValidityRegisterLiveData
    }

    fun validEmail(email: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailRegisterLiveData.value = "Invalid Email Address"
            registerValidity.setEmailValidity(false)
        } else if (!email.matches(Constants.MINONEC.toRegex())) {
            emailRegisterLiveData.value = "Email must contain at least 1 character"
            registerValidity.setEmailValidity(false)
        } else {
            emailRegisterLiveData.value = null
            registerValidity.setEmailValidity(true)
        }

    }

    fun validPassword(password: String) {
        if (!password.matches(Constants.MINSIXC.toRegex())) {
            passwordRegisterLiveData.value = "Password must contain at least 6 character"
            registerValidity.setPasswordValidity(false)
        } else {
            registerValidity.setPasswordMatch(password)
            passwordRegisterLiveData.value = null
            registerValidity.setPasswordValidity(true)
        }

    }

    fun validRepeatPassword(passwordRepeat: String) {
        if (!(passwordRepeat == registerValidity.getPasswordMatch())) {
            passwordRepeatRegisterLiveData.value = "Passwords doesn't match"
            registerValidity.setRepeatPasswordValidity(false)
        } else {
            passwordRepeatRegisterLiveData.value = null
            registerValidity.setRepeatPasswordValidity(true)
        }

    }

    fun checkValidity() {
        checkValidityRegisterLiveData.value = registerValidity.isRegisterValid()
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
                    if (response.isSuccessful) {
                        registrationResultLiveData.value = GenericResponse(response.isSuccessful, "", ResponseStatus.SUCCESS)
                    } else {
                        registrationResultLiveData.value = GenericResponse(false, response.errorBody().toString(), ResponseStatus.FAILURE)

                    }

                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registrationResultLiveData.value = GenericResponse(false, t.localizedMessage, ResponseStatus.FAILURE)
                }
            })
    }
}