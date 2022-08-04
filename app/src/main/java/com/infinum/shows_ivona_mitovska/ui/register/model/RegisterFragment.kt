package com.infinum.shows_ivona_mitovska.ui.register.model

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.databinding.FragmentRegisterBinding
import com.infinum.shows_ivona_mitovska.ui.register.RegisterValidity
import com.infinum.shows_ivona_mitovska.ui.register.viewmodel.RegisterViewModel
import com.infinum.shows_ivona_mitovska.utils.Constants
import com.infinum.shows_ivona_mitovska.utils.Constants.REGISTER_FRAGMENT_RESULT_KEY
import com.infinum.shows_ivona_mitovska.utils.Constants.USER_REGISTRATION_STATUS

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val registerValidity = RegisterValidity()
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenOnRegister()
        validEmailRegisterListener()
        validPasswordRegisterListener()
        validPasswordRepeatListener()
        observeRegister()
    }

    private fun observeRegister() {
        viewModel.getRegistrationResultLiveData().observe(viewLifecycleOwner) {
            if (it.responseStatus == ResponseStatus.SUCCESS) {
                setFragmentResult(REGISTER_FRAGMENT_RESULT_KEY, bundleOf(USER_REGISTRATION_STATUS to true))
            }
            else{
                Toast.makeText(requireContext(), getString(R.string.fail), Toast.LENGTH_LONG).show()
            }
            binding.pBarRegister.isVisible = false
            findNavController().popBackStack()
        }
    }

    private fun listenOnRegister() {
        binding.registerButton.setOnClickListener {
            viewModel.onRegisterButtonClicked(
                email = binding.emailRegisterEditText.text.toString(),
                password = binding.passwordRegisterEditText.text.toString(),
            )
            binding.pBarRegister.isVisible = true

        }
    }

    private fun checkValidity() {
        binding.registerButton.isEnabled = registerValidity.isLoginValid()
    }

    private fun validEmailRegisterListener() {
        binding.emailRegisterEditText.doOnTextChanged { text: CharSequence?, start: Int, count: Int, after: Int ->
            validEmail(text.toString())
            checkValidity()
        }
    }

    private fun validEmail(email: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailRegisterLayout.error = "Invalid Email Address"
            registerValidity.setEmailValidity(false)
        } else if (!email.matches(Constants.MINONEC.toRegex())) {
            binding.emailRegisterLayout.error = "Email must contain at least 1 character"
            registerValidity.setEmailValidity(false)
        } else {
            binding.emailRegisterLayout.error = null
            registerValidity.setEmailValidity(true)
        }

    }

    private fun validPasswordRegisterListener() {
        binding.passwordRegisterEditText.doOnTextChanged { text: CharSequence?, start: Int, count: Int, after: Int ->
            validPassword(text.toString())
            checkValidity()
        }
    }

    private fun validPassword(password: String) {
        if (!password.matches(Constants.MINSIXC.toRegex())) {
            binding.passwordRegisterLayout.error = "Password must contain at least 6 character"
            registerValidity.setPasswordValidity(false)
        } else {
            binding.passwordRegisterLayout.error = null
            registerValidity.setPasswordValidity(true)
        }

    }

    private fun validPasswordRepeatListener() {
        binding.passwordRepeatRegisterEditText.doOnTextChanged { text: CharSequence?, start: Int, count: Int, after: Int ->
            validRepeatPassword(text.toString())
            checkValidity()
        }
    }

    private fun validRepeatPassword(passwordRepeat: String) {
        if (!(passwordRepeat == binding.passwordRegisterEditText.text.toString())) {
            binding.passwordRepeatRegisterLayout.error = "Passwords doesn't match"
            registerValidity.setRepeatPasswordValidity(false)
        } else {
            binding.passwordRepeatRegisterLayout.error = null
            registerValidity.setRepeatPasswordValidity(true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
