package com.infinum.shows_ivona_mitovska.ui.show_details.model

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.ReviewRepository
import com.infinum.shows_ivona_mitovska.databinding.DialogAddReviewBinding
import com.infinum.shows_ivona_mitovska.databinding.FragmentShowDetailsBinding
import com.infinum.shows_ivona_mitovska.ui.show_details.adapter.ReviewsAdapter
import com.infinum.shows_ivona_mitovska.ui.show_details.viewmodel.ShowDetailsViewModel

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val args: ShowDetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<ShowDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.back_arrow)
            setNavigationOnClickListener { view ->
                requireActivity().onBackPressed()
            }
        }

        writeCommentButton()
        initReviewRecycler()
        observeShow()
        observeReviews()
        viewModel.initShow(args.selectedShow)
    }

    private fun observeReviews() {
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviewsAdapter.updateData(reviews)
            updateRating(reviews.size.toString(), viewModel.getAverageReviewRating())
        }
    }

    private fun observeShow() {
        viewModel.show.observe(viewLifecycleOwner) { show ->
            binding.toolbar.title = show.name
            binding.imageDetails.setImageResource(show.imageResId)
            binding.imageDetails.clipToOutline = true
            binding.infoDetails.text = show.info
        }
    }

    private fun writeCommentButton() {
        binding.writeReviewButton.setOnClickListener {
            showDetailsReviewDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRating(size: String, average: String) {
        val value = size == "0"
        binding.emptyRecycler.isVisible = value
        binding.reviewRecycler.isVisible = !value
        binding.ratingBar.isVisible = !value
        binding.averageStars.isVisible = !value
        binding.averageStars.text = "$size reviews, average $average"
        binding.ratingBar.rating = average.toFloat()
    }

    private fun showDetailsReviewDialog() {
        val dialog = BottomSheetDialog(this.requireContext())
        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.submitReviewButton.setOnClickListener {
            if (bottomSheetBinding.reviewRatingBar.rating != 0f) {
                val email = args.username
                val username = email.split("@")
                viewModel.addReview(
                    bottomSheetBinding.commentEditText.text.toString(), username[0],
                    bottomSheetBinding.reviewRatingBar.rating.toInt()
                )
                dialog.dismiss()
            } else {
                Toast.makeText(context, resources.getString(R.string.insert_rating), Toast.LENGTH_LONG).show()
            }
        }
        bottomSheetBinding.closeDialogButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initReviewRecycler() {
        reviewsAdapter = ReviewsAdapter(ReviewRepository.getReviews())
        binding.reviewRecycler.apply {
            layoutManager = LinearLayoutManager(this@ShowDetailsFragment.context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@ShowDetailsFragment.context, LinearLayoutManager.VERTICAL))
            adapter = reviewsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}