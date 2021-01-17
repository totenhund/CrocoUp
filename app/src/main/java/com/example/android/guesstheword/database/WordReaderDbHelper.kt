package com.example.android.guesstheword.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class WordReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    private val SQL_CREATE_ANIMALS_ENTRIES =
            "CREATE TABLE ${WordCategoryContract.WordCategoryEntryAnimals.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${WordCategoryContract.WordCategoryEntryAnimals.WORD} TEXT)"
    private val SQL_CREATE_JOB_ENTRIES =
            "CREATE TABLE ${WordCategoryContract.WordCategoryEntryJobs.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${WordCategoryContract.WordCategoryEntryJobs.WORD} TEXT)"
    private val SQL_CREATE_IDIOMS_ENTRIES =
            "CREATE TABLE ${WordCategoryContract.WordCategoryEntryIdioms.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${WordCategoryContract.WordCategoryEntryIdioms.WORD} TEXT)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${WordCategoryContract.WordCategoryEntryAnimals.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ANIMALS_ENTRIES)
        db.execSQL(SQL_CREATE_JOB_ENTRIES)
        db.execSQL(SQL_CREATE_IDIOMS_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // db.execSQL(SQL_DELETE_ENTRIES)
        // onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}