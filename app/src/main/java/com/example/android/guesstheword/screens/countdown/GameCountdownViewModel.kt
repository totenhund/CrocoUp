package com.example.android.guesstheword.screens.countdown

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.screens.game.GameViewModel
import timber.log.Timber

class GameCountdownViewModel(wordCategory: String) : ViewModel() {

    companion object {
        // when game is over
        private const val DONE = 0L

        // num of ms in 1 sec
        private const val ONE_SECOND = 1000L

        // total time of game
        private const val COUNTDOWN_TIME = 4000L
    }

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

        timer = object : CountDownTimer(GameCountdownViewModel.COUNTDOWN_TIME, GameCountdownViewModel.ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = GameCountdownViewModel.DONE
                _timerFinish.value = true
            }

            override fun onTick(p0: Long) {
                _currentTime.value = (p0 / GameCountdownViewModel.ONE_SECOND)
            }
        }.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Timber.i("timer is canceled")
    }

    fun onTimerComplete() {
        _timerFinish.value = false
    }
}