package com.infinum.shows_ivona_mitovska.ui.show_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.infinum.shows_ivona_mitovska.ReviewRepository
import com.infinum.shows_ivona_mitovska.ShowRepository
import com.infinum.shows_ivona_mitovska.databinding.ActivityShowDetailsBinding
import com.infinum.shows_ivona_mitovska.databinding.DialogAddReviewBinding
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.ui.show_details.adapter.ShowsDetailsAdapter
import com.infinum.shows_ivona_mitovska.utils.Constants.SHOW_SELECTED_KEY

class ShowDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowDetailsBinding
    private lateinit var reviewsAdapter: ShowsDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showDetails()
        writeCommentButton()
        initReviewRecycler()
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.titleDetails)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun showDetails() {
        val id = intent.extras?.getInt(SHOW_SELECTED_KEY)
        binding.titleDetails.title = ShowRepository.getShowById(id!!)?.name
        binding.imageDetails.setBackgroundResource(ShowRepository.getShowById(id)!!.imageResId)
        binding.infoDetails.text = ShowRepository.getShowById(id)?.info
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
        val averageFormatted = average.toString().format("%.2f")
        binding.averageStars.text = "$reviewCount reviews,average $averageFormatted"
        binding.ratingBar.rating = average.toFloat()
    }

    private fun showDetailsReviewDialog() {
        val dialog = BottomSheetDialog(this)
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
                Toast.makeText(this@ShowDetailsActivity, "Please insert rating", Toast.LENGTH_LONG).show()
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
        reviewsAdapter = ShowsDetailsAdapter(ReviewRepository.getReviews())
        binding.reviewRecycler.apply {
            layoutManager = LinearLayoutManager(this@ShowDetailsActivity, VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@ShowDetailsActivity, VERTICAL))
            adapter = reviewsAdapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}