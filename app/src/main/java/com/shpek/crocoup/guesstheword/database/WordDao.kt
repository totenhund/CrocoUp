package com.shpek.crocoup.guesstheword.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// word dao
@Dao
interface WordDao {

    // add word object
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addWord(word: Word)

    // get all data from database
    @Query("SELECT * from words")
    fun readAllData(): LiveData<List<Word>>

    // get all distinct categories
    @Query("SELECT DISTINCT category from words ORDER BY category")
    fun readAllCategories(): LiveData<List<String>>

    // get all distinct categories in russian
    @Query("SELECT DISTINCT categoryRu from words ORDER BY category")
    fun readAllCategoriesRu(): LiveData<List<String>>

    // get words of particular category
    @Query("SELECT word FROM words WHERE category=:category")
    fun readWordsByCategory(category: String): List<String>

    // get words of particular category in russian
    @Query("SELECT wordRu FROM words WHERE categoryRu=:categoryRu")
    fun readWordsRuByCategoryRu(categoryRu: String): List<String>
}