package com.infinum.shows_ivona_mitovska.ui.login.model

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.databinding.FragmentLoginBinding
import com.infinum.shows_ivona_mitovska.persistence.ShowPreferences
import com.infinum.shows_ivona_mitovska.ui.login.LoginValidity
import com.infinum.shows_ivona_mitovska.utils.Constants

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginValidity = LoginValidity()
    private lateinit var prefs: ShowPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = ShowPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenOnLogin()
        validEmailListener()
        validPasswordListener()
    }

    override fun onResume() {
        super.onResume()
        binding.passwordEditText.text?.clear()
    }

    private fun listenOnLogin() {
        val email = binding.emailEditText.text
        binding.loginButton.setOnClickListener {
            if (binding.rememberMeCheckBox.isChecked) {
                prefs.saveString(Constants.USERNAME, email.toString())
            }
            val defaultImage = BitmapFactory.decodeResource(resources, R.drawable.placeholder)
            prefs.saveImageToPrefs(Constants.USER_IMAGE, defaultImage)
            findNavController().navigate(LoginFragmentDirections.toShowsFragment(email.toString()))
        }
    }

    private fun validEmailListener() {
        binding.emailEditText.doOnTextChanged { text: CharSequence?, start: Int, count: Int, after: Int ->
            validEmail(text.toString())
            checkValidity()
        }
    }

    private fun validPasswordListener() {
        binding.passwordEditText.doOnTextChanged { text: CharSequence?, start: Int, count: Int, after: Int ->
            validPassword(text.toString())
            checkValidity()
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}