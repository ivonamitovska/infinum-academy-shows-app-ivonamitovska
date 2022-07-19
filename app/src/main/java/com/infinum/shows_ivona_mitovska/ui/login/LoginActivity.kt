package com.infinum.shows_ivona_mitovska.ui.login


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.infinum.shows_ivona_mitovska.databinding.ActivityLoginBinding
import com.infinum.shows_ivona_mitovska.ui.shows.ShowsActivity
import com.infinum.shows_ivona_mitovska.utils.Constants


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginValidity = LoginValidity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listenOnLogin()
        validEmailListener()
        validPasswordListener()
    }


    private fun listenOnLogin() {
        binding.loginButton.setOnClickListener {
            val intent = Intent(this, ShowsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validEmailListener() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(text: Editable?) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                validEmail(text.toString())
                checkValidity()
            }
        })
    }

    private fun validPasswordListener() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun afterTextChanged(text: Editable?) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                validPassword(text.toString())
                checkValidity()
            }
        })
    }

    private fun checkValidity() {
        binding.loginButton.isEnabled = loginValidity.isLoginValid()
    }

    private fun validEmail(email: String) {
        if (!email.matches(Constants.MINONEC.toRegex())) {
            binding.emailLayout.error = "Email must contain at least 1 character"
            loginValidity.setEmailValidity(false)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.error = "Invalid Email Address"
            loginValidity.setEmailValidity(false)
        } else {
            binding.emailLayout.error = null
            loginValidity.setEmailValidity(true)
        }
    }

    private fun validPassword(password: String) {
        if (!password.matches(Constants.MINSIXC.toRegex())) {
            binding.passwordLayout.error = "Password must contain at least 6 character"
            loginValidity.setPasswordValidity(false)
        } else {
            binding.passwordLayout.error = null
            loginValidity.setPasswordValidity(true)
        }
    }


}