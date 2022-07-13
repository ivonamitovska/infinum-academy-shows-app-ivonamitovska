package com.infinum.shows_ivona_mitovska


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.infinum.shows_ivona_mitovska.databinding.ActivityLoginBinding


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
        binding.LoginButton.setOnClickListener {

            val email = binding.EmailEdittext.text
            val split = email?.split("@")?.toTypedArray()
            val intent = Intent("com.infinum.shows.action")
            intent.putExtra(Intent.EXTRA_TEXT, split?.get(0))
            startActivity(intent)

        }
    }

    private fun validEmailListener() {
        binding.EmailEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validEmail(p0.toString())
                checkValidity()
            }
        })
    }

    private fun validPasswordListener() {
        binding.PasswordEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validPassword(p0.toString())
                checkValidity()
            }
        })
    }

    fun checkValidity() {
        binding.LoginButton.isEnabled = loginValidity.isLoginValid()
    }

    private fun validEmail(email: String) {
//
        if(!email.matches("^.{1,}".toRegex())){
            binding.EmailLayout.error = "Email must contain at least 1 character"
            loginValidity.setEmailValidity(false)
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EmailLayout.error = "Invalid Email Address"
            loginValidity.setEmailValidity(false)
        } else {
            binding.EmailLayout.error = null
            loginValidity.setEmailValidity(true)
        }
    }

    private fun validPassword(password: String) {

        if(!password.matches("^.{6,}".toRegex())){
            binding.PasswordLayout.error = "Password must contain at least 6 character"
            loginValidity.setPasswordValidity(false)
        }
        else {
            binding.PasswordLayout.error = null
            loginValidity.setPasswordValidity(true)
        }
    }
}