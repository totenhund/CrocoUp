package com.shpek.crocoup.guesstheword.screens.game


import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.*
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
import com.shpek.crocoup.guesstheword.R
import com.shpek.crocoup.guesstheword.databinding.GameFragmentBinding
import timber.log.Timber
import java.util.*
import kotlin.math.sqrt

/*Fragment where the game is occur*/
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

    private lateinit var mpCorrect: MediaPlayer
    private lateinit var mpSkip: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        mpCorrect = MediaPlayer.create(activity!!, R.raw.correct)
        mpSkip = MediaPlayer.create(activity!!, R.raw.skip)

        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        viewModelFactory = GameViewModelFactory(activity!!.application, gameFragmentArgs.category)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(GameViewModel::class.java)



        Timber.i("ViewModelProvider is Called!")



        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager!!.registerListener(sensorListener, sensorManager!!
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
                vibrateOnFinish()
                val currentScore = viewModel.score.value ?: 0
                val action = GameFragmentDirections.actionGameToScore(currentScore, viewModel.category.value!!)
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

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > 3 && diff > 810) {

                if (z > 0.5 && y < 9) {
                    viewModel.onSkip()
                    changeBackground(false)
                    Timber.i("z:$z x:$x y:$y a:$acceleration diff: $diff")
                } else if (z < 0.5 && y < 9) {
                    viewModel.onCorrect()
                    changeBackground(true)
                    Timber.i("z:$z x:$x y:$y a:$acceleration diff: $diff")
                }

                lastDate = Date()
            }


        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Timber.i("Accuracy $accuracy")
        }
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


    private fun changeBackground(hasGuessed: Boolean) {

        if (hasGuessed) {
            binding.gameLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.ok_green_color, null))
            binding.guessResultTextView.setText(R.string.correct_answer)
            mpCorrect.start()
        } else {
            binding.gameLayout.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.skip_main_color, null))
            binding.guessResultTextView.setText(R.string.pass_answer)
            mpSkip.start()
        }

        binding.wordText.visibility = View.INVISIBLE
        binding.scoreText.visibility = View.INVISIBLE

        object : CountDownTimer(800, 50) {
            override fun onTick(arg0: Long) {
                Timber.i("Tick $arg0")
            }

            override fun onFinish() {
                binding.gameLayout.setBackgroundColor(Color.parseColor("#949cdf"))
                binding.wordText.visibility = View.VISIBLE
                binding.scoreText.visibility = View.VISIBLE
                binding.guessResultTextView.text = ""
            }
        }.start()
    }

    @Suppress("DEPRECATION")
    private fun vibrateOnFinish() {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } else {
            vibrator.vibrate(400)
        }
    }

}
