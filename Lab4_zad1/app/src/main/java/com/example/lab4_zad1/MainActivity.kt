package com.example.lab4_zad1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var batteryStatusTextView: TextView
    private lateinit var batteryReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        batteryStatusTextView = findViewById(R.id.battery_status_textView)

        batteryReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context, intent: Intent) {
                val action: String? = intent.action

                if (Intent.ACTION_BATTERY_LOW == action) {
                    batteryStatusTextView.text = "Bateria jest rozładowana!"
                }
                else if (Intent.ACTION_BATTERY_OKAY == action) {
                    batteryStatusTextView.text = "Bateria jest w dobrym stanie"
                }
                else if (Intent.ACTION_POWER_CONNECTED == action) {
                    batteryStatusTextView.text = "Telefon podłączony do ładowania"
                }
                else if (Intent.ACTION_POWER_DISCONNECTED == action) {
                    batteryStatusTextView.text = "Telefon odłączony od ładowania"
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(batteryReceiver,filter)
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(batteryReceiver)
    }
}