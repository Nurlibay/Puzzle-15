package uz.unidev.numberpuzzle15.data.pref

import android.content.Context
import android.content.SharedPreferences
import uz.unidev.numberpuzzle15.util.Constants.BEST_SCORE
import uz.unidev.numberpuzzle15.util.Constants.BEST_TIME
import uz.unidev.numberpuzzle15.util.Constants.IS_MUSIC
import uz.unidev.numberpuzzle15.util.Constants.IS_PLAYING
import uz.unidev.numberpuzzle15.util.Constants.MUSIC_POSITION
import uz.unidev.numberpuzzle15.util.Constants.NUMBERS
import uz.unidev.numberpuzzle15.util.Constants.PAUSE_TIME
import uz.unidev.numberpuzzle15.util.Constants.SCORE
import uz.unidev.numberpuzzle15.util.Constants.SHARED_PREFERENCES

class SharedPref private constructor(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    private val editor = preferences.edit()

    var bestMoves: Int
        get() = preferences.getInt(BEST_SCORE, -1)
        set(value) = editor.putInt(BEST_SCORE, value).apply()

    var bestTime: Long
        get() = preferences.getLong(BEST_TIME, -1)
        set(value) = editor.putLong(BEST_TIME, value).apply()

    var numbers: String?
        get() = preferences.getString(NUMBERS, "1#2#3#4#5#6#7#8#9#10#11#12#13#14#15##")
        set(value) = editor.putString(NUMBERS, value).apply()

    var isPlaying: Boolean
        get() = preferences.getBoolean(IS_PLAYING, false)
        set(value) = editor.putBoolean(IS_PLAYING, value).apply()

    var score: Int
        get() = preferences.getInt(SCORE, 0)
        set(value) = editor.putInt(SCORE, value).apply()

    var pauseTime: Long
        get() = preferences.getLong(PAUSE_TIME, 0)
        set(value) = editor.putLong(PAUSE_TIME, value).apply()

    var musicPosition: Int
        get() = preferences.getInt(MUSIC_POSITION, 0)
        set(value) = editor.putInt(MUSIC_POSITION, value).apply()

    var isMusic: Boolean
        get() = preferences.getBoolean(IS_MUSIC, false)
        set(value) = editor.putBoolean(IS_MUSIC, value).apply()

    companion object {
        private lateinit var instance: SharedPref

        fun init(context: Context) {
            instance = SharedPref(context)
        }

        fun getInstance() = instance
    }
}