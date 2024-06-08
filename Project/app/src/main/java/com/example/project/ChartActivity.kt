package com.example.project

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.getSystemService
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ChartActivity : AppCompatActivity() {

    private lateinit var main_button: Button

    private lateinit var sensorManager: SensorManager

    private var gyroChart: LineChart? = null
    private var accelChart: LineChart? = null

    private lateinit var mAccelerometer: Sensor
    private lateinit var mGyroscope: Sensor
    private lateinit var sensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        main_button = findViewById(R.id.go_back_button)

        gyroChart = findViewById(R.id.gyroChart)
        accelChart = findViewById(R.id.accelChart)

        // Inicjalizacja wykresów
        accelChart!!.data = LineData() // Inicjalizacja danych dla akcelerometru
        accelChart!!.axisLeft.setDrawLabels(true) // Włączenie rysowania etykiet na osi Y
        accelChart!!.axisLeft.textColor = Color.WHITE // Ustawienie koloru etykiet na biały
        val accelXDataSet = LineDataSet(null, "X")
        val accelYDataSet = LineDataSet(null, "Y")
        val accelZDataSet = LineDataSet(null, "Z")
        accelXDataSet.colors = listOf(Color.RED)
        accelYDataSet.colors = listOf(Color.GREEN)
        accelZDataSet.colors = listOf(Color.BLUE)
        accelChart!!.data.addDataSet(accelXDataSet) // Dodanie datasetu dla X
        accelChart!!.data.addDataSet(accelYDataSet) // Dodanie datasetu dla Y
        accelChart!!.data.addDataSet(accelZDataSet) // Dodanie datasetu dla Z

        gyroChart!!.data = LineData() // Inicjalizacja danych dla żyroskopu
        gyroChart!!.axisLeft.setDrawLabels(true) // Włączenie rysowania etykiet na osi Y
        gyroChart!!.axisLeft.textColor = Color.WHITE // Ustawienie koloru etykiet na biały
        val gyroXDataSet = LineDataSet(null, "X")
        val gyroYDataSet = LineDataSet(null, "Y")
        val gyroZDataSet = LineDataSet(null, "Z")
        gyroXDataSet.colors = listOf(Color.RED)
        gyroYDataSet.colors = listOf(Color.GREEN)
        gyroZDataSet.colors = listOf(Color.BLUE)
        gyroChart!!.data.addDataSet(gyroXDataSet) // Dodanie datasetu dla X
        gyroChart!!.data.addDataSet(gyroYDataSet) // Dodanie datasetu dla Y
        gyroChart!!.data.addDataSet(gyroZDataSet) // Dodanie datasetu dla Z

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        main_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Nie jest potrzebne w tym przypadku
            }

            private var sampleCount = 0
            private val sampleFrequency = 10 // zmniejszenie częstotliwości próbkowania, np. co piątą aktualizację
            private val maxDataPoints = 10
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    sampleCount++
                    if (sampleCount % sampleFrequency == 0) {
                        if (it.sensor == mAccelerometer) {
                            val xAccel = it.values[0]
                            val yAccel = it.values[1]
                            val zAccel = it.values[2]

                            val accelData = accelChart!!.data

                            if (accelData.dataSetCount > 0) {
                                val xDataSet = accelData.getDataSetByIndex(0)
                                if (xDataSet.entryCount >= maxDataPoints) {
                                    xDataSet.removeFirst()
                                    for (i in 0 until xDataSet.entryCount) {
                                        xDataSet.getEntryForIndex(i).x = i.toFloat()
                                    }
                                }
                                accelData.addEntry(Entry(xDataSet.entryCount.toFloat(), xAccel), 0)

                                val yDataSet = accelData.getDataSetByIndex(1)
                                if (yDataSet.entryCount >= maxDataPoints) {
                                    yDataSet.removeFirst()
                                    for (i in 0 until yDataSet.entryCount) {
                                        yDataSet.getEntryForIndex(i).x = i.toFloat()
                                    }
                                }
                                accelData.addEntry(Entry(yDataSet.entryCount.toFloat(), yAccel), 1)

                                val zDataSet = accelData.getDataSetByIndex(2)
                                if (zDataSet.entryCount >= maxDataPoints) {
                                    zDataSet.removeFirst()
                                    for (i in 0 until zDataSet.entryCount) {
                                        zDataSet.getEntryForIndex(i).x = i.toFloat()
                                    }
                                }
                                accelData.addEntry(Entry(zDataSet.entryCount.toFloat(), zAccel), 2)

                                accelChart!!.notifyDataSetChanged()
                                accelChart!!.setVisibleXRangeMaximum(maxDataPoints.toFloat())
                                accelChart!!.moveViewToX(accelData.entryCount.toFloat())
                            }
                        } else if (it.sensor == mGyroscope) {
                            val xGyro = it.values[0]
                            val yGyro = it.values[1]
                            val zGyro = it.values[2]

                            val gyroData = gyroChart!!.data

                            if (gyroData.dataSetCount > 0) {
                                val xDataSet = gyroData.getDataSetByIndex(0)
                                if (xDataSet.entryCount >= maxDataPoints) {
                                    xDataSet.removeFirst()
                                    for (i in 0 until xDataSet.entryCount) {
                                        xDataSet.getEntryForIndex(i).x = i.toFloat()
                                    }
                                }
                                gyroData.addEntry(Entry(xDataSet.entryCount.toFloat(), xGyro), 0)

                                val yDataSet = gyroData.getDataSetByIndex(1)
                                if (yDataSet.entryCount >= maxDataPoints) {
                                    yDataSet.removeFirst()
                                    for (i in 0 until yDataSet.entryCount) {
                                        yDataSet.getEntryForIndex(i).x = i.toFloat()
                                    }
                                }
                                gyroData.addEntry(Entry(yDataSet.entryCount.toFloat(), yGyro), 1)

                                val zDataSet = gyroData.getDataSetByIndex(2)
                                if (zDataSet.entryCount >= maxDataPoints) {
                                    zDataSet.removeFirst()
                                    for (i in 0 until zDataSet.entryCount) {
                                        zDataSet.getEntryForIndex(i).x = i.toFloat()
                                    }
                                }
                                gyroData.addEntry(Entry(zDataSet.entryCount.toFloat(), zGyro), 2)

                                gyroChart!!.notifyDataSetChanged()
                                gyroChart!!.setVisibleXRangeMaximum(maxDataPoints.toFloat())
                                gyroChart!!.moveViewToX(gyroData.entryCount.toFloat())
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(sensorEventListener,mGyroscope,SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }
}