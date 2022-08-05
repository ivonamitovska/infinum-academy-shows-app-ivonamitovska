package com.infinum.shows_ivona_mitovska.ui.login.model

import android.animation.ValueAnimator
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.databinding.FragmentLoginBinding
import com.infinum.shows_ivona_mitovska.persistence.ShowPreferences
import com.infinum.shows_ivona_mitovska.ui.login.LoginValidity
import com.infinum.shows_ivona_mitovska.ui.login.viemodel.LoginViewModel
import com.infinum.shows_ivona_mitovska.utils.Constants
import com.infinum.shows_ivona_mitovska.utils.Constants.REGISTER_FRAGMENT_RESULT_KEY
import com.infinum.shows_ivona_mitovska.utils.Constants.USER_REGISTRATION_STATUS

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginValidity = LoginValidity()
    private lateinit var prefs: ShowPreferences
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = ShowPreferences(requireContext())

        setFragmentResultListener(REGISTER_FRAGMENT_RESULT_KEY) { _, bundle ->
            if (bundle.getBoolean(USER_REGISTRATION_STATUS)) {
                binding.login.text = getString(R.string.registration_successfull)
                binding.registerLoginButton.isVisible = false
            }
        }
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
        animateShowTriangle()
        animateTitle()
        listenOnLogin()
        validEmailListener()
        validPasswordListener()
        registerButton()
        observeLogin()
    }

    override fun onResume() {
        super.onResume()
        binding.passwordEditText.text?.clear()
    }

    private fun observeLogin() {
        viewModel.getLoginResultLiveData().observe(viewLifecycleOwner) { response ->
            if (response.responseStatus == ResponseStatus.SUCCESS) {
                prefs.saveToken(response.data!!)
                saveRemeberMe(binding.rememberMeCheckBox.isChecked)
                saveDefaultImage()
                findNavController().navigate(LoginFragmentDirections.toShowsFragment())
            } else {
                Toast.makeText(requireContext(), getString(R.string.fail), Toast.LENGTH_LONG).show()
            }
            binding.pBarLogin.isVisible = false
        }
    }

    private fun saveDefaultImage() {
        val defaultImage = BitmapFactory.decodeResource(resources, R.drawable.placeholder)
        prefs.saveImageToPrefs(Constants.USER_IMAGE, defaultImage)
    }

    private fun saveRemeberMe(checked: Boolean) {
        prefs.saveBoolean(Constants.REMEMBER_ME, checked)
    }

    private fun registerButton() {
        binding.registerLoginButton.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
    }

    private fun listenOnLogin() {
        binding.loginButton.setOnClickListener {
            viewModel.onLoginButtonClicked(
                email = binding.emailEditText.text.toString(),
                password = binding.passwordEditText.text.toString()
            )
            binding.pBarLogin.isVisible = true

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

    fun animateShowTriangle()=with(binding.triangle){
        translationY=-500f
        animate()
            .translationY(0f)
            .setDuration(1000)
            .setInterpolator(BounceInterpolator())
            .start()
    }
    fun animateTitle(){
        binding.title.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.zoom))
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}