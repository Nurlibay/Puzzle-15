package uz.nurlibaydev.numberpuzzle15.ui.screens

import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import uz.nurlibaydev.numberpuzzle15.R
import uz.nurlibaydev.numberpuzzle15.data.pref.SharedPref
import uz.nurlibaydev.numberpuzzle15.data.room.AppDatabase
import uz.nurlibaydev.numberpuzzle15.data.room.Result
import uz.nurlibaydev.numberpuzzle15.databinding.ActivityGameBinding
import uz.nurlibaydev.numberpuzzle15.ui.dialog.WinDialog
import uz.nurlibaydev.numberpuzzle15.util.Constants.DIALOG_FRAGMENT
import uz.nurlibaydev.numberpuzzle15.util.Constants.IS_STARTED
import uz.nurlibaydev.numberpuzzle15.util.Constants.MUSIC_ICON_STATE
import uz.nurlibaydev.numberpuzzle15.util.Constants.NUMBERS
import uz.nurlibaydev.numberpuzzle15.util.Constants.PAUSE_TIME
import uz.nurlibaydev.numberpuzzle15.util.Constants.SCORE
import uz.nurlibaydev.numberpuzzle15.util.Coordinate
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.sqrt

/**
 *  Created by Nurlibay Koshkinbaev on 12/08/2022 16:50
 */

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private lateinit var numbers: ArrayList<Int>
    private lateinit var emptyCoordinate: Coordinate
    private lateinit var buttons: Array<Array<Button>>

    private var score = 0
    private var pauseTime: Long = 0
    private var isStarted = false
    private var bestScore = 0
    private var soundButtonStatus = true

    private var mediaPlayer: MediaPlayer? = null
    private var clickMP: MediaPlayer? = null

    private val sharedPref = SharedPref.getInstance()
    private val dao = AppDatabase.getInstance().resultDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadButtons()
        getBestScore()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivRefresh.setOnClickListener {
            onRestartGame()
        }
        binding.ivSound.setOnClickListener {
            onSoundButtonClick(it)
        }

        if (savedInstanceState == null) {
            isStarted = sharedPref.isPlaying
            if (isStarted) {
                score = sharedPref.score
                pauseTime = sharedPref.pauseTime
                val numbersList: MutableList<String> = ArrayList()
                val numbersText = sharedPref.numbers
                val numbersArray = numbersText!!.split("#").toTypedArray()
                for (i in numbersArray.indices) {
                    numbersList.add(numbersArray[i])
                }
                binding.tvScore.text = score.toString()
                loadSavedNumbers(numbersList)
            } else {
                initNumbers()
                loadNumbersToButtons()
            }
        }
    }

    private fun getBestScore() {
        if (sharedPref.bestMoves != -1) {
            bestScore = sharedPref.bestMoves
            binding.tvBest.text = bestScore.toString()
        }
    }

    private fun loadSavedNumbers(numbers: List<String>?) {
        for (i in numbers!!.indices) {
            if (numbers[i] == "") {
                emptyCoordinate = Coordinate(i % 4, i / 4)
                buttons[i/4][i%4].visibility = View.INVISIBLE
            }
            buttons[i / 4][i % 4].text = numbers[i]
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score = savedInstanceState.getInt(SCORE, 0)
        pauseTime = savedInstanceState.getLong(PAUSE_TIME, 0)
        isStarted = savedInstanceState.getBoolean(IS_STARTED)
        soundButtonStatus = savedInstanceState.getBoolean(MUSIC_ICON_STATE)
        val numbersList: List<String>? = savedInstanceState.getStringArrayList(NUMBERS)
        binding.tvScore.text = score.toString()
        loadSavedNumbers(numbersList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SCORE, score)
        outState.putLong(PAUSE_TIME, pauseTime)
        outState.putBoolean(IS_STARTED, isStarted)
        outState.putBoolean(MUSIC_ICON_STATE, soundButtonStatus)
        val numbers = java.util.ArrayList<String>()
        for (i in 0 until binding.groupItems.childCount) {
            numbers.add((binding.groupItems.getChildAt(i) as Button).text.toString())
        }
        outState.putStringArrayList(NUMBERS, numbers)
        super.onSaveInstanceState(outState)
    }

    private fun setSoundButtonStatus() {
        val ivSound = binding.ivSound
        if (!soundButtonStatus) {
            ivSound.setImageResource(R.drawable.ic_volume_off)
            pause()
        } else {
            ivSound.setImageResource(R.drawable.ic_volume_up)
            play()
        }
    }

    private fun onSoundButtonClick(view: View) {
        val ivSound = view as ImageView
        if (soundButtonStatus) {
            soundButtonStatus = false
            ivSound.setImageResource(R.drawable.ic_volume_off)
            mediaPlayer!!.pause()
        } else {
            soundButtonStatus = true
            ivSound.setImageResource(R.drawable.ic_volume_up)
            mediaPlayer!!.start()
        }
    }

    private fun loadButtons() {
        val count = binding.groupItems.childCount
        val size = sqrt(count.toDouble())
        buttons = Array(4) {
            Array(4) {
                Button(this)
            }
        }

        for (i in 0 until binding.groupItems.childCount) {
            val view = binding.groupItems.getChildAt(i)
            val button = view as Button
            button.setOnClickListener {
                onButtonClick(it)
            }
            val y = (i / size).toInt()
            val x = (i % size).toInt()
            button.tag = Coordinate(x, y)
            buttons[y][x] = button
        }
    }

    private fun initNumbers() {
        numbers = ArrayList()
        for (i in 1..15) {
            numbers.add(i)
        }
    }

    private fun loadNumbersToButtons() {
        shuffle()
        for (i in buttons.indices) {
            for (j in buttons.indices) {
                val index = i * 4 + j
                if (index < 15) {
                    buttons[i][j].text = numbers[index].toString()
                }
            }
        }
        buttons[3][3].visibility = View.INVISIBLE
        buttons[3][3].text = ""
        emptyCoordinate = Coordinate(3, 3)
        score = 0
        binding.tvScore.text = score.toString()
        isStarted = true
        binding.tvTime.base = SystemClock.elapsedRealtime()
        binding.tvTime.start()
    }

    private fun onRestartGame() {
        buttons[emptyCoordinate.y][ emptyCoordinate.x].visibility = View.VISIBLE
        initNumbers()
        loadButtons()
        loadNumbersToButtons()
    }

    private fun onButtonClick(view: View) {
        playClickSound()
        val button = view as Button
        val c = button.tag as Coordinate
        val eX = emptyCoordinate.x
        val eY = emptyCoordinate.y
        val dX = abs(c.x - eX)
        val dY = abs(c.y - eY)
        if (dX + dY == 1) {
            score++
            binding.tvScore.text = score.toString()
            buttons[eY][eX].text = button.text
            button.visibility = View.INVISIBLE
            button.text = ""
            buttons[eY][eX].visibility = View.VISIBLE
            emptyCoordinate = c
            if (isWin()) {
                saveBestScore()
                binding.tvTime.stop()
                openWinDialog(score, binding.tvTime.text.toString())
            }
        }
    }

    private fun saveBestTime() {
        val currentTime = binding.tvTime.text.toString()
        var time = 0L
        val list = currentTime.split(":")
        if(list.size == 2) {
            time += list[0].toInt() * 60 + list[1].toInt()
        }
        if(list.size == 3) {
            time += list[0].toInt() * 3600 + list[1].toInt() * 60 + list[3].toInt()
        }
        if (sharedPref.bestTime == -1L) {
            sharedPref.bestTime = time
        } else {
            sharedPref.bestTime = min(sharedPref.bestTime, time)
        }
    }

    private fun saveBestScore() {
        if (binding.tvBest.text.toString() == "-") {
            sharedPref.bestMoves = score
            bestScore = sharedPref.bestMoves
        } else {
            val min = min(score, sharedPref.bestMoves)
            sharedPref.bestMoves = min
            bestScore = min
        }
        binding.tvBest.text = bestScore.toString()
    }

    private fun isWin(): Boolean {
        if (!(emptyCoordinate.x == 3 && emptyCoordinate.y == 3)) return false
        for (i in 0..14) {
            val s = buttons[i / 4][i % 4].text.toString()
            if (s != (i + 1).toString()) return false
        }
        return true
    }

    private fun shuffle() {
        numbers.remove(Integer.valueOf(0))
        numbers.shuffle()
        if (!isSolvable(numbers)) shuffle()
    }

    override fun onResume() {
        super.onResume()
        if (isStarted) {
            binding.tvTime.base = SystemClock.elapsedRealtime() + pauseTime
            binding.tvTime.start()
            play()
        }
        setSoundButtonStatus()
    }

    override fun onPause() {
        super.onPause()
        pauseTime = binding.tvTime.base - SystemClock.elapsedRealtime()
        binding.tvTime.stop()
        putDataToSharedPref()
    }

    private fun putDataToSharedPref() {
        sharedPref.isPlaying = true
        sharedPref.score = score
        sharedPref.pauseTime = pauseTime
        sharedPref.musicPosition = mediaPlayer!!.currentPosition

        val builder = StringBuilder()
        for (i in 0 until binding.groupItems.childCount - 1) {
            builder.append((binding.groupItems.getChildAt(i) as Button).text.toString()).append("#")
        }
        builder.append((binding.groupItems.getChildAt(15) as Button).text.toString())
        sharedPref.numbers = builder.toString()
        sharedPref.isMusic = true
    }

    private fun isSolvable(puzzle: ArrayList<Int>): Boolean {
        numbers.add(0)
        var parity = 0
        val gridWidth = sqrt(puzzle.size.toDouble()).toInt()
        var row = 0 // the current row we are on
        var blankRow = 0 // the row with the blank tile
        for (i in puzzle.indices) {
            if (i % gridWidth == 0) { // advance to next row
                row++
            }
            if (puzzle[i] == 0) { // the blank tile
                blankRow = row // save the row on which encountered
                continue
            }
            for (j in i + 1 until puzzle.size) {
                if (puzzle[i] > puzzle[j] && puzzle[j] != 0) {
                    parity++
                }
            }
        }
        return if (gridWidth % 2 == 0) { // even grid
            if (blankRow % 2 == 0) { // blank on odd row; counting from bottom
                parity % 2 == 0
            } else { // blank on even row; counting from bottom
                parity % 2 != 0
            }
        } else { // odd grid
            parity % 2 == 0
        }
    }

    private fun playClickSound() {
        if (clickMP != null) {
            clickMP!!.release()
        }
        clickMP = MediaPlayer.create(this, R.raw.click)
        clickMP!!.start()
    }

    private fun openWinDialog(moves: Int, time: String) {
        val dialog = WinDialog(moves, time)
        dialog.show(supportFragmentManager, DIALOG_FRAGMENT)
        binding.tvTime.stop()
        saveBestTime()
        dao.insertResult(Result(0, binding.tvTime.text.toString(), score))
        dialog.nextButtonClickListener {
            onRestartGame()
        }

        dialog.homeButtonClickListener {
            onBackPressed()
            onRestartGame()
        }
    }

    private fun play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music)
            //mediaPlayer!!.setOnCompletionListener { stopPlayer() }
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()

        } else {
            mediaPlayer!!.seekTo(sharedPref.musicPosition)
        }
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }

    private fun pause() {
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    private fun stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
            //showMessage("MediaPlayer released !")
        }
    }
}