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
import android.widget.Toast
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
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import timber.log.Timber
import kotlin.math.abs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Fragment for the starting or title screen of the app
 */
class TitleFragment : Fragment() {

    private lateinit var titleViewModel: TitleViewModel
    private lateinit var viewModelFactory: TitleViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: TitleFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.title_fragment, container, false)


        viewModelFactory = TitleViewModelFactory(requireActivity().application)
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
                requireContext(),
                R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewPager2.addItemDecoration(itemDecoration)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        Timber.i(Locale.getDefault().language)

        return binding.root
    }


}
