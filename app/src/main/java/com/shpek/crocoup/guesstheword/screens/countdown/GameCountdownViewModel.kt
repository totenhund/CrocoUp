package com.shpek.crocoup.guesstheword.screens.countdown

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
/*
* ViewModel for GameCountdownFragment
* */
class GameCountdownViewModel(wordCategory: String) : ViewModel() {


    private val timer: CountDownTimer


    private val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category


    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime


    private val _timerFinish = MutableLiveData<Boolean>()
    val timerFinish: LiveData<Boolean>
        get() = _timerFinish



    init {
        _category.value = wordCategory

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
                _timerFinish.value = true
            }

            override fun onTick(p0: Long) {
                _currentTime.value = (p0 / ONE_SECOND)
            }
        }.start()
    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Timber.i("timer is canceled")
    }


    // timer is finished
    fun onTimerComplete() {
        _timerFinish.value = false
    }


    companion object {

        private const val DONE = 0L

        private const val ONE_SECOND = 1000L

        private const val COUNTDOWN_TIME = 4000L
    }

}