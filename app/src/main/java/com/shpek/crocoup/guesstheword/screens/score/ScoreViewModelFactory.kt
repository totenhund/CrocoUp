package com.shpek.crocoup.guesstheword.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScoreViewModelFactory(private val finalScore: Int, private val category: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(finalScore, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}