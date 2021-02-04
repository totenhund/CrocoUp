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
import kotlin.math.acos
import kotlin.math.roundToInt
import kotlin.math.sqrt


/*Fragment where the game is occur*/
class GameFragment : Fragment(){

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var binding: GameFragmentBinding


    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var diff = 0L
    private var last = Calendar.getInstance()
    private var now = Calendar.getInstance()
    var lastDate = Date()

    private lateinit var mpCorrect: MediaPlayer
    private lateinit var mpSkip: MediaPlayer


    private var inclineGravity = FloatArray(3)
    private var mGravity: FloatArray? = FloatArray(3)
    private var mGeomagnetic: FloatArray? = FloatArray(3)
    private var pitch = 0f
    private var roll = 0f

    private var mSensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private lateinit var sensorListener: SensorEventListener


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


        mSensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH



        sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {

                last = Calendar.getInstance()
                last.time = lastDate
                now = Calendar.getInstance()
                now.time = Date()
                diff = now.timeInMillis - last.timeInMillis


                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        mGravity = event.values
                    } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                        mGeomagnetic = event.values
                        if (diff > 810){
                        if (isTiltDownward) {
                            changeBackground(true)
                            viewModel.onCorrect()
                            lastDate = Date()
                        } else if (isTiltUpward) {
                            changeBackground(false)
                            viewModel.onSkip()
                            lastDate = Date()
                        }}
                    }


            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                Timber.i("Accuracy $accuracy")
            }
        }

        initListeners()



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

    private fun initListeners() {
        mSensorManager!!.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
        mSensorManager!!.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onDestroy() {
        mSensorManager!!.unregisterListener(sensorListener)
        super.onDestroy()
    }

    override fun onResume() {
        initListeners()
        super.onResume()
    }

    override fun onPause() {
        mSensorManager!!.unregisterListener(sensorListener)
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


    private val isTiltUpward: Boolean
        get() {
            if (mGravity != null && mGeomagnetic != null) {
                val r = FloatArray(9)
                val i = FloatArray(9)
                val success =
                        SensorManager.getRotationMatrix(r, i, mGravity, mGeomagnetic)
                if (success) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(r, orientation)

                    pitch = orientation[1]
                    roll = orientation[2]
                    inclineGravity = mGravity!!.clone()
                    val normOfG = sqrt(
                            inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + (inclineGravity[2] * inclineGravity[2]).toDouble()
                    )

                    // Normalize the accelerometer vector
                    inclineGravity[0] = (inclineGravity[0] / normOfG).toFloat()
                    inclineGravity[1] = (inclineGravity[1] / normOfG).toFloat()
                    inclineGravity[2] = (inclineGravity[2] / normOfG).toFloat()

                    //Checks if device is flat on ground or not
                    val inclination = Math.toDegrees(
                            acos(
                                    inclineGravity[2].toDouble()
                            )
                    ).roundToInt()

                    val objPitch = pitch
                    val objZero = 0.0f
                    val objZeroPointTwo = 0.2f
                    val objZeroPointTwoNegative = -0.2f
                    val objPitchZeroResult = objPitch.compareTo(objZero)
                    val objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch)
                    val objPitchZeroPointTwoNegativeResult =
                            objPitch.compareTo(objZeroPointTwoNegative)
                    return roll < 0 && (objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0 || objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0) && inclination > 30 && inclination < 40
                }
            }
            return false
        }


    private val isTiltDownward: Boolean
        get() {
            if (mGravity != null && mGeomagnetic != null) {
                val r = FloatArray(9)
                val i = FloatArray(9)
                val success =
                        SensorManager.getRotationMatrix(r, i, mGravity, mGeomagnetic)
                if (success) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(r, orientation)
                    pitch = orientation[1]
                    roll = orientation[2]
                    inclineGravity = mGravity!!.clone()
                    val normOfG = sqrt(
                            inclineGravity[0] * inclineGravity[0] + inclineGravity[1] * inclineGravity[1] + (inclineGravity[2] * inclineGravity[2]).toDouble()
                    )

                    inclineGravity[0] = (inclineGravity[0] / normOfG).toFloat()
                    inclineGravity[1] = (inclineGravity[1] / normOfG).toFloat()
                    inclineGravity[2] = (inclineGravity[2] / normOfG).toFloat()


                    val inclination = Math.toDegrees(
                            acos(
                                    inclineGravity[2].toDouble()
                            )
                    ).roundToInt()
                    val objPitch = pitch
                    val objZero = 0.0f
                    val objZeroPointTwo = 0.2f
                    val objZeroPointTwoNegative = -0.2f
                    val objPitchZeroResult = objPitch.compareTo(objZero)
                    val objPitchZeroPointTwoResult = objZeroPointTwo.compareTo(objPitch)
                    val objPitchZeroPointTwoNegativeResult =
                            objPitch.compareTo(objZeroPointTwoNegative)
                    return roll < 0 && (objPitchZeroResult > 0 && objPitchZeroPointTwoResult > 0 || objPitchZeroResult < 0 && objPitchZeroPointTwoNegativeResult > 0) && inclination > 140 && inclination < 170
                }
            }
            return false
        }

}
