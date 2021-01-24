package com.example.android.guesstheword.screens.title

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.guesstheword.database.Repository
import com.example.android.guesstheword.database.WordDatabase

class TitleViewModel(application: Application): AndroidViewModel(application) {

    val readAllCategories: LiveData<List<String>>

    init {
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        val rep = Repository(wordDao)
        readAllCategories = rep.readAllCategories
    }

}