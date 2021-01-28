package com.shpek.crocoup.guesstheword.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCard(card: Card)

    @Query("SELECT color from cards ORDER BY category")
    fun readColors(): LiveData<List<String>>

    @Query("SELECT icon FROM cards ORDER BY category")
    fun readIcons(): LiveData<List<Int>>

}