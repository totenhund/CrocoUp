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

package com.example.android.guesstheword.screens.game


import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import timber.log.Timber
import java.util.*
import kotlin.math.sqrt


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var binding: GameFragmentBinding

    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var diff = 0L
    private var last = Calendar.getInstance()
    private var now = Calendar.getInstance()
    var lastDate = Date()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Timber.i("ViewModelProvider is Called!")

        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        viewModelFactory = GameViewModelFactory(gameFragmentArgs.category)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(GameViewModel::class.java)


        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH


        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })

        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord.toString()
        })

        viewModel.currentTime.observe(viewLifecycleOwner, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished) {
                val currentScore = viewModel.score.value ?: 0
                val action = GameFragmentDirections.actionGameToScore(currentScore)
                findNavController(this).navigate(action)
                viewModel.onGameFinishComplete()
            }
        })


        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding.root

    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            last = Calendar.getInstance()
            last.time = lastDate
            now = Calendar.getInstance()
            now.time = Date()
            diff = now.timeInMillis - last.timeInMillis

//            Timber.i("Diff%s", diff.toString())

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > 3 && diff > 750) {

                    if (z > 0 && x < 0) {
                        viewModel.onSkip()
                        skipAnswer()
                    }else if (z < 0) {
                        viewModel.onCorrect()
                        correctAnswer()
                    }

                lastDate = Date()
            }


        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }


    private fun correctAnswer(){
        binding.gameLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.ok_green_color, null))
        object : CountDownTimer(1000, 50) {
            override fun onTick(arg0: Long) {

            }

            override fun onFinish() {
                binding.gameLayout.setBackgroundColor(Color.parseColor("#221946"))
            }
        }.start()
    }

    private fun skipAnswer(){
        binding.gameLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.skip_main_color, null))
        object : CountDownTimer(1000, 50) {
            override fun onTick(arg0: Long) {

            }

            override fun onFinish() {
                binding.gameLayout.setBackgroundColor(Color.parseColor("#221946"))
            }
        }.start()
    }


}
