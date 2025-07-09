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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class ChartActivity : AppCompatActivity() {

    private lateinit var mainButton: Button

    private var gyroChart: LineChart? = null
    private var accelChart: LineChart? = null

    private lateinit var sensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mGyroscope: Sensor
    private lateinit var sensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        mainButton = findViewById(R.id.go_back_button)
        gyroChart = findViewById(R.id.gyroChart)
        accelChart = findViewById(R.id.accelChart)

        initializeChart(accelChart, "Accelerometer")
        initializeChart(gyroChart, "Gyroscope")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        mainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

            private var sampleCount = 0
            private val sampleFrequency = 1
            private val maxDataPoints = 10

            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    sampleCount++
                    if (sampleCount % sampleFrequency == 0) {
                        if (it.sensor == mAccelerometer) {
                            updateChart(accelChart, it.values, maxDataPoints)
                        } else if (it.sensor == mGyroscope) {
                            updateChart(gyroChart, it.values, maxDataPoints)
                        }
                    }
                }
            }
        }
    }

    private fun initializeChart(chart: LineChart?, description: String) {
        chart?.apply {
            data = LineData()
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            axisLeft.setDrawLabels(true)
            axisLeft.textColor = Color.WHITE
            this.description.text = description
            this.description.textColor = Color.WHITE
            legend.textColor = Color.WHITE
            val colors = listOf(Color.RED, Color.GREEN, Color.BLUE)
            val labels = listOf("X", "Y", "Z")
            for (i in labels.indices) {
                val dataSet = LineDataSet(null, labels[i])
                dataSet.color = colors[i]
                dataSet.setDrawValues(false)
                data.addDataSet(dataSet)
            }
        }
    }

    private fun updateChart(chart: LineChart?, values: FloatArray, maxDataPoints: Int) {
        chart?.let {
            adjustValues(values)
            val data = it.data
            for (i in values.indices) {
                val dataSet = data.getDataSetByIndex(i)
                if (dataSet.entryCount >= maxDataPoints) {
                    dataSet.removeFirst()
                    for (j in 0 until dataSet.entryCount) {
                        dataSet.getEntryForIndex(j).x = j.toFloat()
                    }
                }
                data.addEntry(Entry(dataSet.entryCount.toFloat(), values[i]), i)
            }
            data.notifyDataChanged()
            it.notifyDataSetChanged()
            it.setVisibleXRangeMaximum(maxDataPoints.toFloat())
            it.moveViewToX(data.entryCount.toFloat())
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(sensorEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }

    private fun adjustValues(values: FloatArray) {
        for (i in values.indices) {
            if (values[i] >= -0.5f && values[i] <= 0.5f) {
                values[i] = 0f
            }
        }
    }
}
