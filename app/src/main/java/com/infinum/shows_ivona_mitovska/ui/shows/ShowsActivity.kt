package com.infinum.shows_ivona_mitovska.ui.shows

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.ShowRepository
import com.infinum.shows_ivona_mitovska.databinding.ActivityShowsBinding
import com.infinum.shows_ivona_mitovska.ui.show_details.ShowDetailsActivity
import com.infinum.shows_ivona_mitovska.ui.shows.adapter.ShowsAdapter
import com.infinum.shows_ivona_mitovska.utils.Constants.SHOW_SELECTED_KEY

class ShowsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowsBinding
    private lateinit var showsAdapter: ShowsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initShowsRecycler()
        binding.buttonEmpty.setOnClickListener {
            if (binding.emptyText.isVisible) {
                binding.emptyText.isVisible = false
                binding.emptyImage.isVisible = false
                binding.showsRecycler.isVisible = true
                binding.buttonEmpty.text = getString(R.string.empty_list_text)
            } else {
                binding.emptyText.isVisible = true
                binding.emptyImage.isVisible = true
                binding.showsRecycler.isVisible = false
                binding.buttonEmpty.text = getString(R.string.show_list_text)
            }

        }

    }

    private fun initShowsRecycler() {
        showsAdapter = ShowsAdapter(ShowRepository.getShows()) { showId ->
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra(SHOW_SELECTED_KEY, showId)
            startActivity(intent)
        }
        binding.showsRecycler.apply {
            layoutManager = LinearLayoutManager(this@ShowsActivity, VERTICAL, false)
            adapter = showsAdapter
        }
    }

}