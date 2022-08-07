package com.infinum.shows_ivona_mitovska.ui.register

class RegisterValidity(
    private var emailValidity: Boolean = false,
    private var passwordValidity: Boolean = false,
    private var repeatPasswordValidity: Boolean = false,
    private var passwordMatch: String? = null
) {
    fun setEmailValidity(value: Boolean) {
        this.emailValidity = value
    }

    fun setPasswordValidity(value: Boolean) {
        this.passwordValidity = value
    }

    fun setRepeatPasswordValidity(value: Boolean) {
        this.repeatPasswordValidity = value
    }

    fun setPasswordMatch(value: String) {
        this.passwordMatch = value
    }

    fun getPasswordMatch(): String? {
        return passwordMatch
    }

    fun isRegisterValid(): Boolean = emailValidity && passwordValidity && repeatPasswordValidity
}