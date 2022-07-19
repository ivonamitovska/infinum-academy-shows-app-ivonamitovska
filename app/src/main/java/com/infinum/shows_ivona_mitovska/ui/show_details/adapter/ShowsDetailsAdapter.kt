package com.infinum.shows_ivona_mitovska.ui.show_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.databinding.ItemReviewBinding
import com.infinum.shows_ivona_mitovska.model.Review

class ShowsDetailsAdapter(
    private var reviews: List<Review>,
) : RecyclerView.Adapter<ShowsDetailsAdapter.ReviewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ReviewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.count()

    fun addReview(review: Review) {
        reviews = reviews + review

        notifyItemInserted(reviews.size)

    }

    fun getAverageReview(): Double {
        var average = 0.0
        reviews.forEach { average += it.review }
        return average / reviews.size
    }

    inner class ReviewsViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.placeholderView.setImageResource(R.drawable.placeholder)
            binding.nameReview.text = review.name
            binding.commentReview.text = review.comment
            binding.numOfStarsComment.text = review.review.toString()
        }

    }

}
