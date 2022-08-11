package com.infinum.shows_ivona_mitovska.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.databinding.ActivityMainBinding
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import com.infinum.shows_ivona_mitovska.persistence.ShowPreferences
import com.infinum.shows_ivona_mitovska.utils.Constants
import kotlinx.android.synthetic.main.fragment_show_details.*

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: ShowPreferences
    private lateinit var binding: ActivityMainBinding
    var keepSplashOnScreen = true
    private val delay = 500L


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, delay)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ApiModule.initRetrofit()
        prefs = ShowPreferences(this)
        val state = savedInstanceState?.getBoolean("saveState")
        if (state == null || !state) {
            checkRemembered()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }


    private fun checkRemembered() {
        val navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
        val rememberMe = prefs.getBoolean(Constants.REMEMBER_ME)
        if (rememberMe && prefs.getToken() != null) {
            navController.navigate(R.id.showsFragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("saveState", true)
        super.onSaveInstanceState(outState)
    }


}
