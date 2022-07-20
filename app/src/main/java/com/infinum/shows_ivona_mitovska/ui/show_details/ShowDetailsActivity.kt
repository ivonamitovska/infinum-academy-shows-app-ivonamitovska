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
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.ReviewRepository
import com.infinum.shows_ivona_mitovska.ShowRepository
import com.infinum.shows_ivona_mitovska.databinding.ActivityShowDetailsBinding
import com.infinum.shows_ivona_mitovska.databinding.DialogAddReviewBinding
import com.infinum.shows_ivona_mitovska.model.Review
import com.infinum.shows_ivona_mitovska.ui.show_details.adapter.ReviewsAdapter
import com.infinum.shows_ivona_mitovska.utils.Constants.SHOW_SELECTED_KEY
import java.text.DecimalFormat

class ShowDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowDetailsBinding
    private lateinit var reviewsAdapter: ReviewsAdapter
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
        setSupportActionBar(binding.toolbarTitle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun showDetails() {
        val id = intent.extras?.getInt(SHOW_SELECTED_KEY)
        val show = ShowRepository.getShowById(id!!)
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
                Toast.makeText(this@ShowDetailsActivity, resources.getString(R.string.insert_rating), Toast.LENGTH_LONG).show()
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