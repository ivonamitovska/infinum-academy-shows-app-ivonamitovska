package com.infinum.shows_ivona_mitovska.ui.shows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_ivona_mitovska.databinding.ItemShowBinding
import com.infinum.shows_ivona_mitovska.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private var onItemClickCallback: (Int) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding =ItemShowBinding.inflate(LayoutInflater.from(parent.context))
        return ShowsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    inner class ShowsViewHolder(private val binding: ItemShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Show) {
            binding.showName.text = item.name
            binding.showImage.setImageResource(item.imageResId)
            binding.showInfo.text = item.info
            binding.cardShow.setOnClickListener {
                onItemClickCallback(item.id)
            }
        }
    }
}