package com.infinum.shows_ivona_mitovska.ui.register.model

import android.os.Bundle
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

        viewModel.getEmailRegisterLiveData().observe(viewLifecycleOwner) {
            binding.emailRegisterLayout.error = it
        }
        viewModel.getPasswordRegisterLiveData().observe(viewLifecycleOwner) {
            binding.passwordRegisterLayout.error = it
        }
        viewModel.getPasswordRepeatRegisterLiveData().observe(viewLifecycleOwner) {
            binding.passwordRepeatRegisterLayout.error = it
        }

        viewModel.getRegisterValidityLiveData().observe(viewLifecycleOwner) {
            binding.registerButton.isEnabled = it
        }
    }

    private fun observeRegister() {
        viewModel.getRegistrationResultLiveData().observe(viewLifecycleOwner) {
            if (it.responseStatus == ResponseStatus.SUCCESS) {
                setFragmentResult(REGISTER_FRAGMENT_RESULT_KEY, bundleOf(USER_REGISTRATION_STATUS to true))
            } else {
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

    private fun validEmailRegisterListener() {
        binding.emailRegisterEditText.doOnTextChanged { text: CharSequence?, start: Int, count: Int, after: Int ->
            viewModel.validEmail(text.toString())
            viewModel.checkValidity()
        }
    }

    private fun validPasswordRegisterListener() {
        binding.passwordRegisterEditText.doOnTextChanged { text1: CharSequence?, start: Int, count: Int, after: Int ->
            viewModel.validPassword(text1.toString())
            viewModel.checkValidity()

        }
    }

    private fun validPasswordRepeatListener() {
        binding.passwordRepeatRegisterEditText.doOnTextChanged { text: CharSequence?, start: Int, count: Int, after: Int ->
            viewModel.validRepeatPassword(text.toString())
            viewModel.checkValidity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
