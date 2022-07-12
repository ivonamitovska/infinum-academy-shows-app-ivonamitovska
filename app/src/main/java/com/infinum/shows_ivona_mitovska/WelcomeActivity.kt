package com.infinum.shows_ivona_mitovska

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.infinum.shows_ivona_mitovska.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.WelcomeText.text= "Welcome, ${intent.extras?.getString(Intent.EXTRA_TEXT)}!"
    }
}