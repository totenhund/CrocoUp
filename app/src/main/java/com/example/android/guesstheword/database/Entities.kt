package com.example.android.guesstheword.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
        @PrimaryKey(autoGenerate = true)
        val uid: Int,
        @ColumnInfo(name = "word") val word: String?,
        @ColumnInfo(name = "category") val category: String?
)

@Entity(tableName = "cards")
data class Card(
        @PrimaryKey val category: String,
        @ColumnInfo(name = "color") val color: String?,
        @ColumnInfo(name = "icon") val icon: Int?
)