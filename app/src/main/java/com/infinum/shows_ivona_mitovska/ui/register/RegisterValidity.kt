package com.infinum.shows_ivona_mitovska.ui.register


class RegisterValidity(
    private var emailValidity: Boolean = false,
    private var passwordValidity: Boolean = false,
private var repeatPasswordValidity:Boolean=false
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

    fun isLoginValid(): Boolean = emailValidity && passwordValidity && repeatPasswordValidity
}