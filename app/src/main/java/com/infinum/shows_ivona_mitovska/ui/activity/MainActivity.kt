package com.infinum.shows_ivona_mitovska.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.databinding.ActivityMainBinding
import com.infinum.shows_ivona_mitovska.persistence.ShowPreferences
import com.infinum.shows_ivona_mitovska.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: ShowPreferences
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = ShowPreferences(this)
        checkRemembered()
    }

    private fun checkRemembered() {
        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
        val username = prefs.getString(Constants.USERNAME)
        if (!username.isNullOrEmpty()) {
            val bundle: Bundle = bundleOf()
            bundle.putString("username", username)
            navController.navigate(R.id.showsFragment, bundle)
        }
    }


}
