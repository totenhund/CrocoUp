package com.example.android.guesstheword.screens.countdown

import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameCountdownFragmentBinding
import com.example.android.guesstheword.databinding.ScoreFragmentBinding
import com.example.android.guesstheword.screens.game.GameFragmentDirections
import com.example.android.guesstheword.screens.score.ScoreFragmentArgs
import com.example.android.guesstheword.screens.score.ScoreViewModel
import com.example.android.guesstheword.screens.score.ScoreViewModelFactory
import com.example.android.guesstheword.screens.title.TitleFragmentDirections
import timber.log.Timber

class GameCountdownFragment : Fragment() {

    companion object {
        fun newInstance() = GameCountdownFragment()
    }

    private lateinit var viewModel: GameCountdownViewModel
    private lateinit var viewModelFactory: GameCountdownViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: GameCountdownFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_countdown_fragment,
                container,
                false
        )


        val countdownFragmentArgs by navArgs<GameCountdownFragmentArgs>()

        viewModelFactory = GameCountdownViewModelFactory(countdownFragmentArgs.category)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(GameCountdownViewModel::class.java)

        viewModel = ViewModelProvider(this).get(GameCountdownViewModel::class.java)

        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


        viewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
            binding.counterTextView.text = (newTime).toString()

        })

        viewModel.timerFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished) {
                val action = GameCountdownFragmentDirections.actionGameCountdownFragmentToGameDestination2(viewModel.category.value!!)
                NavHostFragment.findNavController(this).navigate(action)
                viewModel.onTimerComplete()
            }
        })
        binding.circularProgressBar.setProgressWithAnimation(100f, 3500)
        return binding.root
    }

}