package com.shpek.crocoup.guesstheword.database

import android.provider.BaseColumns

object WordCategoryContract {
    // Table contents are grouped together in an anonymous object.
    object WordCategoryEntryAnimals : BaseColumns {
        const val TABLE_NAME = "animals"
        const val WORD = "word"
    }

    object WordCategoryEntryJobs : BaseColumns {
        const val TABLE_NAME = "jobs"
        const val WORD = "word"
    }

    object WordCategoryEntryIdioms : BaseColumns {
        const val TABLE_NAME = "idioms"
        const val WORD = "word"
    }

    object CardEntry: BaseColumns {
        const val TABLE_NAME = "Card"
        const val TABLE_CARD = "table_card"
        const val ICON = "icon"
        const val COLOR = "color"
    }
}