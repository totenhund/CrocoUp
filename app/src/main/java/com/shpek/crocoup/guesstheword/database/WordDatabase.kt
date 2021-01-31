package com.shpek.crocoup.guesstheword.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*class for accessing database*/
@Database(entities = [Word::class, Card::class], version = 1, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {

    // wordDao
    abstract fun wordDao(): WordDao
    // cardDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: WordDatabase? = null
        // get database instance
        fun getDatabase(context: Context): WordDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, WordDatabase::class.java, "Database")
                        .createFromAsset("database/word_database.db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}