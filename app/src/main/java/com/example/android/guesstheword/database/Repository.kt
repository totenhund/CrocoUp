package com.example.android.guesstheword.database

import androidx.lifecycle.LiveData

class Repository(private val wordDao: WordDao){

    val readAllData: LiveData<List<Word>> = wordDao.readAllData()
    val readAllCategories: List<String> = wordDao.readAllCategories()

    suspend fun addWord(word: Word){
        wordDao.addWord(word)
    }

}