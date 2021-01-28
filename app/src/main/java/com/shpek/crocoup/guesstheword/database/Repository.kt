package com.shpek.crocoup.guesstheword.database

import androidx.lifecycle.LiveData

class Repository(private val wordDao: WordDao, private val cardDao: CardDao) {

    val readAllData: LiveData<List<Word>> = wordDao.readAllData()
    val readAllCategories: LiveData<List<String>> = wordDao.readAllCategories()
    val readAllCategoriesRu: LiveData<List<String>> = wordDao.readAllCategoriesRu()
    fun readWordsByCategory(category: String): List<String> = wordDao.readWordsByCategory(category)
    fun readWordsRuByCategoryRu(categoryRu: String): List<String> = wordDao.readWordsRuByCategoryRu(categoryRu)

    fun addWord(word: Word) {
        wordDao.addWord(word)
    }

    val readColors: LiveData<List<String>> = cardDao.readColors()
    val readIcons: LiveData<List<Int>> = cardDao.readIcons()

    fun addCard(card: Card) {
        cardDao.addCard(card)
    }

}