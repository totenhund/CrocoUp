package com.example.android.guesstheword.screens.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModelFactory (private val application: Application, private val wordCategory: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(application, wordCategory) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}