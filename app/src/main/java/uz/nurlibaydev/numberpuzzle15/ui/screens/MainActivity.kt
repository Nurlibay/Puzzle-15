package uz.nurlibaydev.numberpuzzle15.ui.screens

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import uz.nurlibaydev.numberpuzzle15.BuildConfig
import uz.nurlibaydev.numberpuzzle15.R
import uz.nurlibaydev.numberpuzzle15.databinding.ActivityMainBinding

/**
 *  Created by Nurlibay Koshkinbaev on 12/08/2022 16:44
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.playNow.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        binding.statistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        binding.info?.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

        binding.share.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
            var shareMessage = "Puzzle 15 game for Android. Develop your brain".trim() + "\n"
            shareMessage = "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}".trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share This App"))
        } catch (e: java.lang.Exception) {
            e.toString()
        }
    }

}