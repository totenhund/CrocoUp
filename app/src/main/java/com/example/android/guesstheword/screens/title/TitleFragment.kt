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

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.android.guesstheword.R
import com.example.android.guesstheword.database.*
import com.example.android.guesstheword.databinding.TitleFragmentBinding
import timber.log.Timber
import kotlin.math.abs


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Fragment for the starting or title screen of the app
 */
class TitleFragment : Fragment() {

    // TODO database should be prepopulate
    var animals = arrayOf("Cat","Cow","Sheep","Rabbit","Duck","Chicken","Horse","Squirrel","Snail","Mouse","chameleon","Deer","raccoon","Snake","Bat","Turtle","Octopus","Frog","whale","crab","clam","fish","lobster","shark","seahorse","desert","Lion","elephant","Cheetah")
    var jobs = arrayOf("Astronaut","Chef","Construction","Firefighter","Doctor","Policia","Teacher","Nurse", "veterinarian","actress","actor","architect","singer","dentist","detective","writer", "farmer","nurse","pilot","engineer","accountant","butcher","cashier","barber", "carpenter","lifeguard","baker","electrician", "flight","attendant","plumber","receptionist", "researcher","scientist","lawyer","bus","driver","designer", "journalist","photographer","musician","painter", "florist","sales","assistant","mechanic","model","shop","assistant","politician", "translator","postman","hairdresser","taxi","driver","pharmacist","nanny", "travel","agent","cleaner","biologist","businesswoman", "businessman","dancer","gardener","meteorologist","programmer","travel","guide","saleswoman","salesman")
    var idioms = arrayOf("Johnny Depp","Arnold Schwarzenegger","Jim Carrey","Emma Watson","Daniel Radcliffe","Leonardo DiCaprio","Tom Cruise","Brad Pitt","Charles Chaplin","Morgan Freeman","Tom Hanks","Hugh Jackman","Matt Damon","Sylvester Stallone","Will Smith","Clint Eastwood","Cameron Diaz","George Clooney","Steven Spielberg","Harrison Ford","Robert De Niro","Al Pacino","Robert Downey Jr.","Russell Crowe","Liam Neeson","Kate Winslet","Mark Wahlberg","Natalie Portman","Pierce Brosnan","Sean Connery","Orlando Bloom","Dwayne Johnson","Jackie Chan","Angelina Jolie","Adam Sandler","Scarlett Johansson","Heath Ledger","Anne Hathaway","Jessica Alba","Edward Norton","Keira Knightley","Bradley Cooper","Will Ferrell","Julia Roberts","Nicolas Cage","Daniel Craig","Keanu Reeves","Ian McKellen","Halle Berry","Bruce Willis","Samuel L. Jackson","Ben Stiller","Tommy Lee Jones","Antonio Banderas","Denzel Washington","Steve Carell","Shia LaBeouf","Megan Fox","James Franco","Mel Gibson","Vin Diesel","Tim Allen","Robin Williams",
            "Kevin Spacey","Jason Biggs","Seann William Scott","Jean-Claude Van Damme","Zach Galifianakis","Owen Wilson","Christian Bale","Peter Jackson","Sandra Bullock","Bruce Lee","Drew Barrymore","Macaulay Culkin","Jack Nicholson","Bill Murray","Sigourney Weaver","Jake Gyllenhaal","Kamal Haasan","Jason Statham","Jet Li","Kate Beckinsale","Rowan Atkinson","Marlon Brando","John Travolta","Channing Tatum","Ben Affleck","Shah Rukh Khan","Jennifer Aniston","Emma Stone","Chris Hemsworth","James McAvoy","James Cameron","Amitabh Bachchan","Brendan Fraser","Rachel McAdams","Tom Hiddleston","Aamir Khan","Rajinikanth")
    var icons = arrayOf(0x1F98A, 0x1F4BC, 0x1F929)
    var colors = arrayOf("#faf0af", "#a3ddcb", "#cd5d7d")

    private lateinit var titleViewModel: TitleViewModel
    private lateinit var viewModelFactory: TitleViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: TitleFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.title_fragment, container, false)


//



        viewModelFactory = TitleViewModelFactory(activity!!.application)
        titleViewModel = ViewModelProvider(this, viewModelFactory)
                .get(TitleViewModel::class.java)


        var adapter = ViewPagerAdapter() {
            Log.i("logi", "clicked at : $it")
            val wordCategory = it
            val action = TitleFragmentDirections.actionTitleDestinationToGameCountdownFragment2(wordCategory)
            findNavController(this).navigate(action)
        }
        binding.viewPager2.adapter = adapter
        binding.viewPager2.offscreenPageLimit = 1
        titleViewModel.readAllCategories.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        titleViewModel.allIcons.observe(viewLifecycleOwner, Observer {
            adapter.setIcons(it)
        })

        titleViewModel.allColors.observe(viewLifecycleOwner, Observer {
            adapter.setColors(it)
        })



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
