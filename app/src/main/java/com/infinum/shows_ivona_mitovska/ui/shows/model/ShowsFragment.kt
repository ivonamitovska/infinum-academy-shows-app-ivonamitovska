package com.infinum.shows_ivona_mitovska.ui.shows.model

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.data.response.generic.ResponseStatus
import com.infinum.shows_ivona_mitovska.database.ShowApplication
import com.infinum.shows_ivona_mitovska.database.ShowViewModelFactory
import com.infinum.shows_ivona_mitovska.database.ShowsDatabase
import com.infinum.shows_ivona_mitovska.databinding.DialogInfoProfileBinding
import com.infinum.shows_ivona_mitovska.databinding.FragmentShowsBinding
import com.infinum.shows_ivona_mitovska.dialogs.Dialogs.showLogOutDialog
import com.infinum.shows_ivona_mitovska.dialogs.Dialogs.showQuitAppDialog
import com.infinum.shows_ivona_mitovska.persistence.ShowPreferences
import com.infinum.shows_ivona_mitovska.ui.shows.adapter.ShowsAdapter
import com.infinum.shows_ivona_mitovska.ui.shows.viewmodel.ShowsViewModel
import com.infinum.shows_ivona_mitovska.utils.Constants
import com.infinum.shows_ivona_mitovska.utils.Constants.REMEMBER_ME

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var showsAdapter: ShowsAdapter
    private val viewModel: ShowsViewModel by viewModels {
        ShowViewModelFactory((activity?.application as ShowApplication).database,requireActivity().application)
    }
    private lateinit var prefs: ShowPreferences
    private val cameraPhotoLauncher = initCameraLauncher()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = ShowPreferences(requireContext())
        handleOnBackPress()
        initShowsRecycler()
        initProfilePhoto()
        initChipListener()
        observeShowList()
        observeTopRatedShows()
        binding.pBarShows.isVisible = true
        val savedState = savedInstanceState?.getBoolean("topRated")
        if (savedState == null || !savedState) {
            getShows()
        } else {
            getTopRated()
        }
    }

    private fun observeTopRatedShows() {
        viewModel.showListTopRated.observe(viewLifecycleOwner) { response ->
            if (response.responseStatus == ResponseStatus.SUCCESS) {
                if (response.data?.size == 0) {
                    binding.groupId.isVisible = true
                    binding.showsRecycler.isVisible = false
                } else {
                    showsAdapter.updateDataTopRated(response.data)
                    binding.groupId.isVisible = false
                    binding.showsRecycler.isVisible = true
                }
            } else {
                Toast.makeText(requireContext(), response.errorMsg, Toast.LENGTH_LONG).show()
            }
            binding.pBarShows.isVisible = false
        }
    }

    private fun observeShowList() {
        viewModel.showList.observe(viewLifecycleOwner) { response ->
            if (response.responseStatus == ResponseStatus.SUCCESS) {
                if (response.data?.size == 0) {
                    binding.groupId.isVisible = true
                    binding.showsRecycler.isVisible = false
                } else {
                    showsAdapter.updateData(response.data)
                    binding.groupId.isVisible = false
                    binding.showsRecycler.isVisible = true
                }
            } else {
                Toast.makeText(requireContext(), response.errorMsg, Toast.LENGTH_LONG).show()
            }
            binding.pBarShows.isVisible = false
        }
    }

    private fun getShows() {
        val token = prefs.getToken()
        if (token != null) {
            viewModel.initShows(token)
        } else {
            Toast.makeText(requireContext(), R.string.must_login, Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }

    }

    private fun initChipListener() {
        binding.topRatedChip.setOnClickListener {
            binding.pBarShows.isVisible = true
            if (binding.topRatedChip.isChecked) {
                getTopRated()
            } else {
                getShows()
            }
        }
    }

    private fun getTopRated() {
        val token = prefs.getToken()
        if (token != null) {
            viewModel.topRated(token)
        }
    }

    private fun handleOnBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showQuitAppDialog(requireContext()) { dialog, id ->
                    requireActivity().finish()
                    dialog.cancel()
                }
            }
        })
    }

    private fun initProfilePhoto() {
        binding.profilePhotoImage.apply {
            setImageBitmap(prefs.getImageFromPrefs(Constants.USER_IMAGE))
            setOnClickListener {
                showProfileInfoDialog()
            }
        }
    }

    private fun showProfileInfoDialog() {
        val dialog = BottomSheetDialog(this.requireContext())
        val bottomSheetBinding = DialogInfoProfileBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.emailDialog.text = prefs.getToken()?.uid
        bottomSheetBinding.profilePhotoDialog.setImageBitmap(prefs.getImageFromPrefs(Constants.USER_IMAGE))
        bottomSheetBinding.logOutButton.setOnClickListener {
            showLogOutDialog(requireContext()) { dialog, id ->
                findNavController().popBackStack()
                prefs.removeString(REMEMBER_ME)
                prefs.deleteToken()
                dialog.cancel()
            }
            dialog.dismiss()
        }
        bottomSheetBinding.changeProfilePhotoButton.setOnClickListener {
            takeCameraPhoto()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun takeCameraPhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraPhotoLauncher.launch(intent)
    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(listOf()) { show ->
            findNavController().navigate(ShowsFragmentDirections.toShowDetailsFragment(show))
        }
        binding.showsRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = showsAdapter
        }
    }

    private fun initCameraLauncher() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            binding.profilePhotoImage.setImageBitmap(imageBitmap)
            prefs.saveImageToPrefs(Constants.USER_IMAGE, imageBitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("topRated", binding.topRatedChip.isChecked)
        super.onSaveInstanceState(outState)
    }
}