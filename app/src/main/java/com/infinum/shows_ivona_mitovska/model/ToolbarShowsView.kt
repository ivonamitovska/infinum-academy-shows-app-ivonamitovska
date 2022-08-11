package com.infinum.shows_ivona_mitovska.model

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.infinum.shows_ivona_mitovska.databinding.ToolbarBinding
import com.infinum.shows_ivona_mitovska.listener.ToolbarListener

class ToolbarShowsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var listener: ToolbarListener
    private var binding: ToolbarBinding

    init {
        binding = ToolbarBinding.inflate(LayoutInflater.from(context), this)
        binding.topRatedChip.setOnClickListener {
            listener.onTopRatedClicked(binding.topRatedChip.isChecked)
        }
        binding.profilePhotoImage.setOnClickListener {
            listener.onProfileClicked()
        }
    }

    fun initToolbar(image: Bitmap?, listener: ToolbarListener) {
        this.listener = listener
        binding.profilePhotoImage.setImageBitmap(image)
    }

    fun changeProfilePicture(image: Bitmap) {
        binding.profilePhotoImage.setImageBitmap(image)
    }
}