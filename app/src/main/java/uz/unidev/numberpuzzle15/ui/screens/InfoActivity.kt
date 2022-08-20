package uz.unidev.numberpuzzle15.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.unidev.numberpuzzle15.databinding.ActivityInfoBinding


/**
 *  Created by Nurlibay Koshkinbaev on 12/08/2022 16:44
 */

class InfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}