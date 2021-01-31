package com.shpek.crocoup.guesstheword.screens.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModelFactory (private val application: Application, private val wordCategory: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(application, wordCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}