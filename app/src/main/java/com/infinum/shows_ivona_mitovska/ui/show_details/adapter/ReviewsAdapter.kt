package com.infinum.shows_ivona_mitovska.ui.show_details.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    fun updateData(shows: List<Review>?) {
        if (shows != null) {
            reviews = shows
            notifyDataSetChanged()
        }
    }

    inner class ReviewsViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.apply {
                placeholderView.setImageResource(R.drawable.placeholder)
                nameReview.text = review.name
                if (review.comment.isNotEmpty()) {
                    commentReview.visibility = View.VISIBLE
                    commentReview.text = review.comment
                }
                numOfStarsComment.text = review.review.toString()
            }
        }
    }

}
