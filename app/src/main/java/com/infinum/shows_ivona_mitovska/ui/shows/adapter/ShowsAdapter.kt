package com.infinum.shows_ivona_mitovska

import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infinum.shows_ivona_mitovska.databinding.CardShowBinding

class ShowsAdapter (
    private var items: List<Show>,
    private var onItemClickCallback: (Show) -> Unit
    ):RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>(){
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding=CardShowBinding.inflate(LayoutInflater.from(parent.context))
      return ShowsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int){
       holder.bind(items[position])
    }

    override fun getItemCount()=items.count()


    inner class ShowsViewHolder(private val binding: CardShowBinding):RecyclerView.ViewHolder(binding.root) {
           fun bind(item:Show){
               binding.showName.text=item.name
               binding.showImage.setImageResource(item.imageResId)
               binding.showInfo.text=item.info

               binding.cardShow.setOnClickListener(){
                   onItemClickCallback(item)
               }
           }
    }
}