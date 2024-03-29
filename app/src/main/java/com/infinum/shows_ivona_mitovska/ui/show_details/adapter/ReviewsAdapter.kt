package com.infinum.shows_ivona_mitovska.ui.show_details.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.databinding.ItemReviewBinding
import com.infinum.shows_ivona_mitovska.model.Review

class ReviewsAdapter(
    private var reviews: List<Review>,
) : RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ReviewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.count()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(reviewsList: List<Review>?) {
        if (reviewsList != null) {
            reviews = reviewsList
            notifyDataSetChanged()
        }
    }

    inner class ReviewsViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.apply {
                placeholderView.setImageResource(R.drawable.placeholder)
                nameReview.text = review.user.email
                if (review.comment.isNotEmpty()) {
                    commentReview.isVisible=true
                    commentReview.text = review.comment
                }
                numOfStarsComment.text = review.rating.toString()
            }
        }
    }

}
