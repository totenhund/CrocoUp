package com.example.android.guesstheword.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addWord(word: Word)

    @Query("SELECT * from words")
    fun readAllData(): LiveData<List<Word>>

    @Query("SELECT DISTINCT category from words ORDER BY category")
    fun readAllCategories(): LiveData<List<String>>

    @Query("SELECT DISTINCT categoryRu from words ORDER BY category")
    fun readAllCategoriesRu(): LiveData<List<String>>

    @Query("SELECT word FROM words WHERE category=:category")
    fun readWordsByCategory(category: String): List<String>

    @Query("SELECT wordRu FROM words WHERE categoryRu=:categoryRu")
    fun readWordsRuByCategoryRu(categoryRu: String): List<String>
}