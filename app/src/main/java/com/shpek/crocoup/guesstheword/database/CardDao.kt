package com.shpek.crocoup.guesstheword.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/*card dao*/
@Dao
interface CardDao {

    // add card object
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCard(card: Card)

    // get all colors
    @Query("SELECT color from cards ORDER BY category")
    fun readColors(): LiveData<List<String>>

    // get all icons (emoji)
    @Query("SELECT icon FROM cards ORDER BY category")
    fun readIcons(): LiveData<List<Int>>

}