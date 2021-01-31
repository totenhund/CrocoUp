package com.shpek.crocoup.guesstheword.screens.logs

import android.app.Application
import timber.log.Timber

/*For timber library*/
@Suppress("unused")
class PusherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}