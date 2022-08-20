package uz.unidev.numberpuzzle15.app

import android.app.Application
import timber.log.Timber
import uz.unidev.numberpuzzle15.BuildConfig
import uz.unidev.numberpuzzle15.data.pref.SharedPref
import uz.unidev.numberpuzzle15.data.room.AppDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
        AppDatabase.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}