package com.example.project

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var acc_x_txt: TextView
    private lateinit var acc_y_txt: TextView
    private lateinit var acc_z_txt: TextView

    private lateinit var gyro_x_txt: TextView
    private lateinit var gyro_y_txt: TextView
    private lateinit var gyro_z_txt: TextView

    private lateinit var progr_bar: ProgressBar

    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mGyroscope: Sensor
    private lateinit var sensorEventListener: SensorEventListener

    private lateinit var chart_button: Button
    private lateinit var gest_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var totallmoveAcc: Double = 0.0
        var totallmoveGyro: Double = 0.0
        var changeAcc: Double = 0.0
        acc_x_txt = findViewById(R.id.acc_x_text)
        acc_y_txt = findViewById(R.id.acc_y_text)
        acc_z_txt = findViewById(R.id.acc_z_text)

        gyro_x_txt = findViewById(R.id.gyro_x_text)
        gyro_y_txt = findViewById(R.id.gyro_y_text)
        gyro_z_txt = findViewById(R.id.gyro_z_text)

        progr_bar = findViewById(R.id.progressBar)

        chart_button = findViewById(R.id.graph_button)
        gest_button = findViewById(R.id.gesture_button)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        chart_button.setOnClickListener{
            val intent = Intent(this, ChartActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        gest_button.setOnClickListener{
            val intent = Intent(this, GestureActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        // Inicjalizacja sensorEventListener
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // Sprawdzamy, z którego sensora pochodzą dane
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        // obsługa akcelerometru
                        val accX = event.values[0]
                        val accY = event.values[1]
                        val accZ = event.values[2]

                        changeAcc = Math.sqrt((accX * accX + accY * accY + accZ * accZ).toDouble())
                        totallmoveAcc = changeAcc/10
                        // Aktualizacja TextView
                        acc_x_txt.text = "X: $accX"
                        acc_y_txt.text = "Y: $accY"
                        acc_z_txt.text = "Z: $accZ"

                        // Aktualizacja movementbaru
                        progr_bar.setProgress(totallmoveAcc.toInt())
                    }
                    Sensor.TYPE_GYROSCOPE -> {
                        // obsluga zyroskopu
                        val gyroX = event.values[0]
                        val gyroY = event.values[1]
                        val gyroZ = event.values[2]
                        totallmoveGyro = Math.sqrt((gyroX * gyroX + gyroY * gyroY + gyroZ * gyroZ).toDouble())

                        // Aktualizacja TextView
                        gyro_x_txt.text = "X: $gyroX"
                        gyro_y_txt.text = "Y: $gyroY"
                        gyro_z_txt.text = "Z: $gyroZ"

                        // Aktualizacja movementbaru
                        progr_bar.setProgress(totallmoveGyro.toInt())
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // To jest nie potrzebne
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        mSensorManager.registerListener(sensorEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        // Wyrejestrowanie listenera
        mSensorManager.unregisterListener(sensorEventListener)
    }
}