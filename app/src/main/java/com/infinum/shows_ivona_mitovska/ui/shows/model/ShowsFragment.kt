package com.infinum.shows_ivona_mitovska.ui.shows.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.ShowRepository
import com.infinum.shows_ivona_mitovska.databinding.FragmentShowsBinding
import com.infinum.shows_ivona_mitovska.ui.shows.adapter.ShowsAdapter

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var showsAdapter: ShowsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShowsRecycler()
        binding.buttonEmpty.setOnClickListener {
            if (binding.emptyText.isVisible) {
                binding.groupId.isVisible = false
                binding.showsRecycler.isVisible = true
                binding.buttonEmpty.text = getString(R.string.empty_list_text)
            } else {
                binding.groupId.isVisible = true
                binding.showsRecycler.isVisible = false
                binding.buttonEmpty.text = getString(R.string.show_list_text)
            }

        }

        binding.logOutButton.setOnClickListener {
            findNavController().navigate(R.id.toLogInFragment)
        }

    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(ShowRepository.getShows()) { showId ->

            findNavController().navigate(ShowsFragmentDirections.toShowDetailsFragment(showId))

        }
        binding.showsRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = showsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}