package com.example.android.guesstheword.database

import androidx.lifecycle.LiveData

class Repository(private val wordDao: WordDao) {

    val readAllData: LiveData<List<Word>> = wordDao.readAllData()
    val readAllCategories: LiveData<List<String>> = wordDao.readAllCategories()
    fun readWordsByCategory(category: String): List<String> = wordDao.readWordsByCategory(category)

    fun addWord(word: Word) {
        wordDao.addWord(word)
    }

//    fun readColorByCategory(category: String): String = cardDao.readColorByCategory(category)
//    fun readIconByCategory(category: String): Int = cardDao.readIconByCategory(category)
//
//    fun addCard(card: Card) {
//        cardDao.addCard(card)
//    }


}