package com.example.android.guesstheword.screens.title

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android.guesstheword.database.Repository
import com.example.android.guesstheword.database.WordDatabase
import java.util.*

class TitleViewModel(application: Application): AndroidViewModel(application) {

    val readAllCategories: LiveData<List<String>>
    val allIcons: LiveData<List<Int>>
    val allColors: LiveData<List<String>>

    init {
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        val cardDao = WordDatabase.getDatabase(application).cardDao()
        val rep = Repository(wordDao, cardDao)

        readAllCategories = if(Locale.getDefault().language == "ru"){
            rep.readAllCategoriesRu
        } else {
            rep.readAllCategories
        }

        allIcons = rep.readIcons
        allColors = rep.readColors
    }

}