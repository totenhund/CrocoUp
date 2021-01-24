package com.example.android.guesstheword.screens.countdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameCountdownViewModelFactory(private val wordCategory: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameCountdownViewModel::class.java)) {
            return GameCountdownViewModel(wordCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}