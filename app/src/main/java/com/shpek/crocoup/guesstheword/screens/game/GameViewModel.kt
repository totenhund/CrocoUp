package com.shpek.crocoup.guesstheword.screens.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shpek.crocoup.guesstheword.database.Repository
import com.shpek.crocoup.guesstheword.database.WordDatabase
import timber.log.Timber
import java.util.*


class GameViewModel(application: Application, wordCategory: String) : AndroidViewModel(application) {

    companion object {
        // when game is over
        private const val DONE = 0L

        // num of ms in 1 sec
        private const val ONE_SECOND = 1000L

        // total time of game
        private const val COUNTDOWN_TIME = 60000L
    }

    private val timer: CountDownTimer

    private val _category = MutableLiveData<String>()
    val category: LiveData<String>
        get() = _category
    //

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    private var wordLiveList: List<String>

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    init {
        Timber.i("GameViewModel is created!")
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        val cardDao = WordDatabase.getDatabase(application).cardDao()
        val rep = Repository(wordDao, cardDao)
        _category.value = wordCategory
        wordLiveList = if (Locale.getDefault().language == "ru") {
            rep.readWordsRuByCategoryRu(wordCategory)
        } else {
            rep.readWordsByCategory(wordCategory)
        }
        Timber.i("Init word list size ${wordLiveList.size}")
        resetList()
        nextWord()
        _score.value = 0


        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }

            override fun onTick(p0: Long) {
                _currentTime.value = (p0 / ONE_SECOND)
            }
        }.start()


        // DateUtils.formatElapsedTime()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Timber.i("timer is canceled")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf()
        for (word in wordLiveList) {
            wordList.add(word)
        }
        Timber.i("word list size: %s", wordLiveList.size.toString())
        wordList.shuffle()
    }


    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
//        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }


}