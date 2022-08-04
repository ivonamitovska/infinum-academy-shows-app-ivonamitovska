package com.infinum.shows_ivona_mitovska.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.infinum.shows_ivona_mitovska.R
import com.infinum.shows_ivona_mitovska.databinding.ActivityMainBinding
import com.infinum.shows_ivona_mitovska.networking.ApiModule
import com.infinum.shows_ivona_mitovska.persistence.ShowPreferences
import com.infinum.shows_ivona_mitovska.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var prefs: ShowPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1300)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ApiModule.initRetrofit()
        prefs = ShowPreferences(this)
        val state = savedInstanceState?.getBoolean("saveState")
        if (state == null || !state) {
            checkRemembered()
        }

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
