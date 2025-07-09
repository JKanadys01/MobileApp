package com.example.project

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class GestureActivity : AppCompatActivity() {

    private lateinit var mainButton: Button
    private lateinit var detectionButton: Button
    private lateinit var rotationButton: Button
    private lateinit var statustext: TextView
    private lateinit var rotstatustext: TextView
    private lateinit var historytext: TextView

    private lateinit var sensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mGyroscope: Sensor
    private lateinit var sensorEventListener: SensorEventListener

    private var isDetectionStarted = false
    private var isRotationDetectionStarted = false
    private var initialOrientation: FloatArray? = null
    private var initialAcceleration: FloatArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)

        mainButton = findViewById(R.id.main_button)
        detectionButton = findViewById(R.id.start_gest_button)
        rotationButton = findViewById(R.id.rotation_det_button)
        rotstatustext = findViewById(R.id.rot_status_text)
        statustext = findViewById(R.id.detection_state_text)
        historytext = findViewById(R.id.history_textView)

        mainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                        if (event.sensor == mAccelerometer && isDetectionStarted) {
                            handleAccelerationEvent(event.values)
                        } else if (event.sensor == mGyroscope && isRotationDetectionStarted) {
                            handleGyroscopeEvent(event.values)
                        }
                }
            }
        }
        detectionButton.setOnClickListener {
            if (!isDetectionStarted) {
                startDetection()
                isDetectionStarted = true
                detectionButton.text = "Stop Detection"
            } else {
                stopDetection()
                isDetectionStarted = false
                detectionButton.text = "Start Detection"
            }
        }

        rotationButton.setOnClickListener {
            if (!isRotationDetectionStarted) {
                startRotDetection()
                isRotationDetectionStarted = true
                rotationButton.text = "Stop Rotation Detection"
            } else {
                stopRotDetection()
                isRotationDetectionStarted = false
                rotationButton.text = "Start Rotation Detection"
            }
        }
    }

    private fun startDetection() {
        initialAcceleration = null
        sensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        statustext.text = "Detection started..."
    }

    private fun startRotDetection() {
        initialOrientation = null
        sensorManager.registerListener(sensorEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        rotstatustext.text = "Deteciton started..."
    }

    private fun stopDetection() {
        sensorManager.unregisterListener(sensorEventListener)
        statustext.text = "Detection stopped"
    }

    private fun stopRotDetection() {
        sensorManager.unregisterListener(sensorEventListener)
        rotstatustext.text = "Deteciton stoped"
    }

    private fun handleAccelerationEvent(values: FloatArray) {
        if (initialAcceleration == null) {
            initialAcceleration = values.clone()
            return
        }
        val deltaX = values[0] - initialAcceleration!![0]
        val deltaY = values[1] - initialAcceleration!![1]
        val deltaZ = values[2] - initialAcceleration!![2]

        val THRESHOLD = 2.0f

        if (deltaX > THRESHOLD) {
            showToast("Move to the right")
        } else if (deltaX < -THRESHOLD) {
            showToast("Move to the left")
        }

        if (deltaY > THRESHOLD) {
            showToast("Move forwards")
        } else if (deltaY < -THRESHOLD) {
            showToast("Move backwards")
        }

        if (deltaZ > THRESHOLD) {
            showToast("Move upward")
        } else if (deltaZ < -THRESHOLD) {
            showToast("Move downward")
        }
    }

    private fun handleGyroscopeEvent(values: FloatArray) {
        if (initialOrientation == null) {
            initialOrientation = values.clone()
            return
        }

        val deltaX = values[0] - initialOrientation!![0]
        val deltaY = values[1] - initialOrientation!![1]
        val deltaZ = values[2] - initialOrientation!![2]

        val THRESHOLD = 2.0f

        if (deltaX > THRESHOLD) {
            showToast("Rotation backwards")
        } else if (deltaX < -THRESHOLD) {
            showToast("Rotation forwards")
        }

        if (deltaY > THRESHOLD) {
            showToast("Rotation right Y")
        } else if (deltaY < -THRESHOLD) {
            showToast("Rotation left Y")
        }

        if (deltaZ > THRESHOLD) {
            showToast("Rotation left Z")
        } else if (deltaZ < -THRESHOLD) {
            showToast("Rotation right Z")
        }
    }

    private var lastToastTime: Long = 0

    private fun showToast(message: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastToastTime > 1000) { // Minimum czas odstępu między komunikatami: 1 sekunda
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            lastToastTime = currentTime
            historytext.append("$message\n")
        }
    }

    override fun onPause() {
        super.onPause()
        stopDetection()
        stopRotDetection()
    }

}