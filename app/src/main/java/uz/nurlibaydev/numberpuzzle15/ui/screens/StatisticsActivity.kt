package uz.nurlibaydev.numberpuzzle15.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.nurlibaydev.numberpuzzle15.data.pref.SharedPref
import uz.nurlibaydev.numberpuzzle15.data.room.AppDatabase
import uz.nurlibaydev.numberpuzzle15.data.room.Result
import uz.nurlibaydev.numberpuzzle15.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding
    private val sharedPref = SharedPref.getInstance()
    private val dao = AppDatabase.getInstance().resultDao()
    private var list = mutableListOf<Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = dao.getAllResults()

        binding.apply {
            backLayout.setOnClickListener {
                onBackPressed()
            }

            if (sharedPref.bestMoves == -1) {
                tvMoves.text = "-"
            } else {
                tvMoves.text = sharedPref.bestMoves.toString()
            }

            if (sharedPref.bestTime == -1L) {
                tvTime.text = "-"
            } else {
                val time = sharedPref.bestTime
                dao.getAllResults().forEach {
                    val timeFormat = it.time.split(":")
                    var realTime = 0L
                    if(timeFormat.size == 2) {
                        realTime = (timeFormat[0].toInt() * 60 + timeFormat[1].toInt()).toLong()
                    }
                    if(timeFormat.size == 3){
                        realTime = (timeFormat[0].toInt() * 3600 + timeFormat[1].toInt() * 60 + timeFormat[2].toInt()).toLong()
                    }
                    if(time == realTime) {
                        tvTime.text = it.time
                    }
                }
            }

            list.sortBy {
                it.moves
            }

            if (list.size >= 3) {
                tvMoves1.text = list[0].moves.toString()
                tvMoves2.text = list[1].moves.toString()
                tvMoves3.text = list[2].moves.toString()

                tvTime1.text = list[0].time
                tvTime2.text = list[1].time
                tvTime3.text = list[2].time
            } else {
                when (list.size) {
                    2 -> {
                        tvMoves1.text = list[0].moves.toString()
                        tvMoves2.text = list[1].moves.toString()
                        tvMoves3.text = "-"

                        tvTime1.text = list[0].time
                        tvTime2.text = list[1].time
                        tvTime3.text = "-"
                    }
                    1 -> {
                        tvMoves1.text = list[0].moves.toString()
                        tvMoves2.text = "-"
                        tvMoves3.text = "-"

                        tvTime1.text = list[0].time
                        tvTime2.text = "-"
                        tvTime3.text = "-"
                    }
                    else -> {
                        tvMoves1.text = "-"
                        tvMoves2.text = "-"
                        tvMoves3.text = "-"

                        tvTime1.text = "-"
                        tvTime2.text = "-"
                        tvTime3.text = "-"
                    }
                }
            }
        }
    }
}