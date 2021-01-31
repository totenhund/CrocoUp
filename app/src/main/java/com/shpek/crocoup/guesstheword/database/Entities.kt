package com.shpek.crocoup.guesstheword.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
*Entries for database
* */
@Entity(tableName = "words")
data class Word(
        @PrimaryKey(autoGenerate = true)
        val uid: Int,
        @ColumnInfo(name = "word") val word: String?,
        @ColumnInfo(name = "wordRu") val wordRu: String?,
        @ColumnInfo(name = "category") val category: String?,
        @ColumnInfo(name = "categoryRu") val categoryRu: String?
)

@Entity(tableName = "cards")
data class Card(
        @PrimaryKey val category: String,
        @ColumnInfo(name = "color") val color: String?,
        @ColumnInfo(name = "icon") val icon: Int?
)