package com.infinum.shows_ivona_mitovska.ui.shows.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.databinding.ItemShowBinding
import com.infinum.shows_ivona_mitovska.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private var onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ItemShowBinding.inflate(LayoutInflater.from(parent.context))
        return ShowsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount() = items.count()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(showList: List<Show>?) {
        if (showList != null) {
            items = showList
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataTopRated(showTopRatedList: List<Show>?) {
        if (showTopRatedList != null) {
            items = showTopRatedList
            notifyDataSetChanged()
        }
    }

    inner class ShowsViewHolder(private val binding: ItemShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.showName.text = item.title
            Glide.with(this.itemView)
                .load(item.imageUrl)
                .placeholder(R.drawable.severance)
                .into(binding.showImage)
            binding.showInfo.text = item.description
            binding.cardShow.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }
}