package com.example.android.guesstheword.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CardDao {

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun addCard(card: Card)
//
//    @Query("SELECT color from cards WHERE category=:category")
//    fun readColorByCategory(category: String): String
//
//    @Query("SELECT icon FROM cards WHERE category=:category")
//    fun readIconByCategory(category: String): Int

}