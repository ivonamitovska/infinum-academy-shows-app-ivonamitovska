package com.infinum.shows_ivona_mitovska.ui.show_details.model

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.ReviewRepository
import com.infinum.shows_ivona_mitovska.ShowRepository
import com.infinum.shows_ivona_mitovska.databinding.DialogAddReviewBinding
import com.infinum.shows_ivona_mitovska.databinding.FragmentShowDetailsBinding
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.ui.show_details.adapter.ReviewsAdapter
import java.text.DecimalFormat

class ShowDetailsFragment : Fragment() {
    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewsAdapter: ReviewsAdapter
    private val args: ShowDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDetails()
        writeCommentButton()
        initReviewRecycler()

    }

    private fun showDetails() {
        val showId = args.showId
        val show = ShowRepository.getShowById(showId)
        binding.toolbarTitle.title = show?.name
        binding.imageDetails.setImageResource(show!!.imageResId)
        binding.imageDetails.clipToOutline = true
        binding.infoDetails.text = show.info
    }

    private fun writeCommentButton() {
        binding.writeReviewButton.setOnClickListener {
            showDetailsReviewDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateRating() {
        val reviewCount = reviewsAdapter.itemCount
        val average = reviewsAdapter.getAverageReview()
        val df = DecimalFormat("#.##")
        val averageFormatted = df.format(average)
        binding.averageStars.text = "$reviewCount reviews, average $averageFormatted"
        binding.ratingBar.rating = average.toFloat()
    }

    private fun showDetailsReviewDialog() {
        val dialog = BottomSheetDialog(this.requireContext())
        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.submitReviewButton.setOnClickListener {
            if (bottomSheetBinding.reviewRatingBar.rating != 0f) {
                addReviewToList(
                    bottomSheetBinding.commentEditText.text.toString(),
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

    private fun addReviewToList(comment: String, review: Int) {
        reviewsAdapter.addReview(Review(comment, "name", review))
        changeReviewVisibility()
        updateRating()
    }

    private fun changeReviewVisibility() {
        val value = reviewsAdapter.itemCount == 0
        binding.emptyRecycler.isVisible = value
        binding.reviewRecycler.isVisible = !value
        binding.ratingBar.isVisible = !value
        binding.averageStars.isVisible = !value
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