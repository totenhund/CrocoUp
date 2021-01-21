/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.title

import android.content.ContentValues
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.WordCategoryContract
import com.example.android.guesstheword.database.WordReaderDbHelper
import com.example.android.guesstheword.databinding.TitleFragmentBinding
import timber.log.Timber
import kotlin.math.abs

/**
 * Fragment for the starting or title screen of the app
 */
class TitleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: TitleFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.title_fragment, container, false)

        val dbHelper = WordReaderDbHelper(activity!!)
        var db = dbHelper.writableDatabase

        // TODO database should be prepopulate
//        var animals = arrayOf("Cat","Cow","Sheep","Rabbit","Duck","Chicken","Horse","Squirrel","Snail","Mouse","chameleon","Deer","raccoon","Snake","Bat","Turtle","Octopus","Frog","whale","crab","clam","fish","lobster","shark","seahorse","desert","Lion","elephant","Cheetah")
//        var jobs = arrayOf("Astronaut","Chef","Construction","Firefighter","Doctor","Policia","Teacher","Nurse", "veterinarian","actress","actor","architect","singer","dentist","detective","writer",
//                "farmer","nurse","pilot","engineer","accountant","butcher","cashier","barber",
//                "carpenter","lifeguard","baker","electrician",
//                "flight","attendant","plumber","receptionist",
//                "researcher","scientist","lawyer","bus","driver","designer",
//                "journalist","photographer","musician","painter",
//                "florist","sales","assistant","mechanic","model","shop","assistant","politician",
//                "translator","postman","hairdresser","taxi","driver","pharmacist","nanny",
//                "travel","agent","cleaner","biologist","businesswoman",
//                "businessman","dancer","gardener","meteorologist","programmer","travel","guide","saleswoman","salesman")
//        var idioms = arrayOf("a blessing in disguise","a sandwich short of a picnic","a stone’s throw","actions speak louder than words","add fuel to the fire","add insult to injury","all ears","at a crossroads","barking up the wrong tree","beat about the bush","better late than never","between a rock and a hard place","bite off more than one can chew","bite the bullet","blow off steam","bob’s your uncle","bog-standard","botch/bodge job","budge up","builder’s tea","ury one’s head in the sand","bust one’s chops","by the skin of one’s teeth","call a spade a spade","call it a day","cheap as chips","chinese whispers","chip on one’s shoulder","clam up","cold feet","(the) cold shoulder","cost a bomb","cost an arm and a leg","couch potato","couldn’t care less","curiosity killed the cat","cut a long story short","cut corners","cut someone some slack","cut to the chase","dig one’s heels in","dog eat dog (also ‘cut throat’)","don’t give up the day job","don’t put all your eggs in one basket","don’t run before you can walk","desperate times call for desperate measures","easy does it","at a horse","(the) elephant in the room","every cloud has a silver lining (often just: every cloud…)","face the music","find one’s feet","finger in every pie","(a) fish out of water","fit as a fiddle","follow in someone’s footsteps","freak out","full of beans","get off one’s back","get out of hand","get over something","get something out of one’s system","get up/out on the wrong side of bed","get one’s act/sh*t together","give someone the benefit of the doubt","glad to see the back of","go back to the drawing board","go cold turkey","go down that road","go the extra mile","(the) grass is always greener (on the other side)","green fingers","hang in there","have eyes in the back of one’s head","head over heels (in love)","heard it on the grapevine","hit the books","it the nail on the head","hit the road","hit the sack","hold your horses","ignorance is bliss","it’s not rocket science","jump on the bandwagon","jump ship","keep one’s chin up","kill two birds with one stone","leave no stone unturned","let someone off the hook","let the cat out of the bag","look like a million dollars","lose one’s touch","miss the boat","nip (something) in the bud","no pain, no gain","no-brainer","not one’s cup of tea","off one’s trolley/rocker/nut/head","off the top of one’s head","on the ball")
//
//        for (word in animals) {
//            val values = ContentValues().apply {
//                put(WordCategoryContract.WordCategoryEntryAnimals.WORD, word)
//            }
//            val newRowId = db?.insert(WordCategoryContract.WordCategoryEntryAnimals.TABLE_NAME, null, values)
//        }
//
//        for (word in jobs) {
//            val values = ContentValues().apply {
//                put(WordCategoryContract.WordCategoryEntryJobs.WORD, word)
//            }
//            val newRowId = db?.insert(WordCategoryContract.WordCategoryEntryJobs.TABLE_NAME, null, values)
//        }
//
//        for (word in idioms) {
//            val values = ContentValues().apply {
//                put(WordCategoryContract.WordCategoryEntryIdioms.WORD, word)
//            }
//            val newRowId = db?.insert(WordCategoryContract.WordCategoryEntryIdioms.TABLE_NAME, null, values)
//        }



        var table = "table"
        var cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null)
        var tableNames = ArrayList<String>()
        while (cursor.moveToNext()){
            tableNames.add(cursor.getString( cursor.getColumnIndex("name")))
        }


        binding.viewPager2.adapter = ViewPagerAdapter(tableNames) {
            Log.i("logi", "clicked at : $it")
            val wordCategory = it
            val action = TitleFragmentDirections.actionTitleDestinationToGameCountdownFragment2(wordCategory)
            findNavController(this).navigate(action)
        }
        binding.viewPager2.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * abs(position))
        }
        binding.viewPager2.setPageTransformer(pageTransformer)
        val itemDecoration = HorizontalMarginItemDecoration(
                context!!,
                R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewPager2.addItemDecoration(itemDecoration)

        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT




        return binding.root
    }
}
