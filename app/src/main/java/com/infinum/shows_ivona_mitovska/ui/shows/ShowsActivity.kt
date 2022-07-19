package com.infinum.shows_ivona_mitovska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.infinum.shows_ivona_mitovska.databinding.ActivityLoginBinding
import com.infinum.shows_ivona_mitovska.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsAdapter


    private val shows=listOf(
        Show("The Office",R.drawable.officeshow,"A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium."),
        Show("Stranger Things",R.drawable.strangerthings,"When a young boy disappears, his mother, a police chief and his friends must confront terrifying supernatural forces in order to get him back."),
        Show("Dark Winds",R.drawable.darkwinds,"Follows Leaphorn and Chee, two Navajo police officers in the 1970s Southwest that are forced to challenge their own spiritual beliefs when they search for clues in a double murder case."),
        Show("Severance",R.drawable.severance,"Mark leads a team of office workers whose memories have been surgically divided between their work and personal lives. When a mysterious colleague appears outside of work, it begins a journey to discover the truth about their jobs."),
        Show("Only murders in the building",R.drawable.onlymurder,"Three strangers who share an obsession with true crime suddenly find themselves caught up in one."),
        Show("Peaky Blinders",R.drawable.peakyblinders,"TA gangster family epic set in 1900s England, centering on a gang who sew razor blades in the peaks of their caps, and their fierce boss Tommy Shelby."),
        Show("Man vs Bee",R.drawable.manvsbee,"A man finds himself at war with a bee while housesitting a luxurious mansion. Who will win, and what irreparable damage will be done in the process?")


    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShowsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initShowsRecycler()
    }
    private fun initShowsRecycler(){
        adapter= ShowsAdapter(shows){ show ->
            val intent = Intent(this,ShowDetailsActivity::class.java)
            startActivity(intent)

        }
         binding.showsRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }


}