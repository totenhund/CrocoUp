package com.example.android.guesstheword.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: Word)

    @Query("SELECT * from word")
    fun readAllData(): LiveData<List<Word>>

    @Query("SELECT category from word")
    fun readAllCategories(): List<String>

}