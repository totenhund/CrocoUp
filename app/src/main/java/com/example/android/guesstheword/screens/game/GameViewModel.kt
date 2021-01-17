package com.example.android.guesstheword.screens.game

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber


class GameViewModel(wordCategory: Int) : ViewModel() {

    companion object {
        // when game is over
        private const val DONE = 0L
        // num of ms in 1 sec
        private const val ONE_SECOND = 1000L
        // total time of game
        private const val COUNTDOWN_TIME = 60000L
    }

    private val timer: CountDownTimer

    //    temporary
    var animals = arrayOf("Cat", "Cow", "Sheep", "Rabbit", "Duck", "Chicken", "Horse", "Squirrel", "Snail", "Mouse", "chameleon", "Deer", "raccoon", "Snake", "Bat", "Turtle", "Octopus", "Frog", "whale", "crab", "clam", "fish", "lobster", "shark", "seahorse", "desert", "Lion", "elephant", "Cheetah")
    var jobs = arrayOf("Astronaut", "Chef", "Construction", "Firefighter", "Doctor", "Policia", "Teacher", "Nurse", "veterinarian", "actress", "actor", "architect", "singer", "dentist", "detective", "writer", "farmer", "nurse", "pilot", "engineer", "accountant", "butcher", "cashier", "barber", "carpenter", "lifeguard", "baker", "electrician", "flight", "attendant", "plumber", "receptionist", "researcher", "scientist", "lawyer", "bus", "driver", "designer", "journalist", "photographer", "musician", "painter", "florist", "sales", "assistant", "mechanic", "model", "shop", "assistant", "politician", "translator", "postman", "hairdresser", "taxi", "driver", "pharmacist", "nanny", "travel", "agent", "cleaner", "biologist", "businesswoman", "businessman", "dancer", "gardener", "meteorologist", "programmer", "travel", "guide", "saleswoman", "salesman")
    var idioms = arrayOf("a blessing in disguise", "a sandwich short of a picnic", "a stone’s throw", "actions speak louder than words", "add fuel to the fire", "add insult to injury", "all ears", "at a crossroads", "barking up the wrong tree", "beat about the bush", "better late than never", "between a rock and a hard place", "bite off more than one can chew", "bite the bullet", "blow off steam", "bob’s your uncle", "bog-standard", "botch/bodge job", "budge up", "builder’s tea", "ury one’s head in the sand", "bust one’s chops", "by the skin of one’s teeth", "call a spade a spade", "call it a day", "cheap as chips", "chinese whispers", "chip on one’s shoulder", "clam up", "cold feet", "(the) cold shoulder", "cost a bomb", "cost an arm and a leg", "couch potato", "couldn’t care less", "curiosity killed the cat", "cut a long story short", "cut corners", "cut someone some slack", "cut to the chase", "dig one’s heels in", "dog eat dog (also ‘cut throat’)", "don’t give up the day job", "don’t put all your eggs in one basket", "don’t run before you can walk", "desperate times call for desperate measures", "easy does it", "at a horse", "(the) elephant in the room", "every cloud has a silver lining (often just: every cloud…)", "face the music", "find one’s feet", "finger in every pie", "(a) fish out of water", "fit as a fiddle", "follow in someone’s footsteps", "freak out", "full of beans", "get off one’s back", "get out of hand", "get over something", "get something out of one’s system", "get up/out on the wrong side of bed", "get one’s act/sh*t together", "give someone the benefit of the doubt", "glad to see the back of", "go back to the drawing board", "go cold turkey", "go down that road", "go the extra mile", "(the) grass is always greener (on the other side)", "green fingers", "hang in there", "have eyes in the back of one’s head", "head over heels (in love)", "heard it on the grapevine", "hit the books", "it the nail on the head", "hit the road", "hit the sack", "hold your horses", "ignorance is bliss", "it’s not rocket science", "jump on the bandwagon", "jump ship", "keep one’s chin up", "kill two birds with one stone", "leave no stone unturned", "let someone off the hook", "let the cat out of the bag", "look like a million dollars", "lose one’s touch", "miss the boat", "nip (something) in the bud", "no pain, no gain", "no-brainer", "not one’s cup of tea", "off one’s trolley/rocker/nut/head", "off the top of one’s head", "on the ball")


    private val _category = MutableLiveData<Int>()
    val category: LiveData<Int>
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

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    init {
        Timber.i("GameViewModel is created!")
        _category.value = wordCategory
        resetList()
        nextWord()
        _score.value = 0


        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinish.value = true
            }

            override fun onTick(p0: Long) {
                _currentTime.value = (p0 / ONE_SECOND)
            }
        }

        timer.start()
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
        var tableName = "animals"
        if (_category.value == 0){
            for (word in animals){
                wordList.add(word)
            }
        }else if(_category.value == 1){
            for (word in jobs){
                wordList.add(word)
            }
        }

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
        _score.value = (score.value)?.minus(1)
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