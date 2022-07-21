package com.infinum.shows_ivona_mitovska.ui.login

class LoginValidity(
    private var emailValidity: Boolean = false,
    private var passwordValidity: Boolean = false
) {
    fun setEmailValidity(value: Boolean) {
        this.emailValidity = value
    }

    fun setPasswordValidity(value: Boolean) {
        this.passwordValidity = value
    }

    fun isLoginValid(): Boolean = emailValidity && passwordValidity
}