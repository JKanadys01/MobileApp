package com.example.lab2

import android.media.Rating
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RatingBar
import android.widget.TextView
import kotlin.math.cos

class MainActivity : AppCompatActivity() {
    private lateinit var costEdittext: EditText
    private lateinit var tipPrecentageEditText: EditText
    private lateinit var serviceQualiyBar: RatingBar
    private lateinit var foodQualityBar: RatingBar
    private lateinit var calculateButton: Button
    private lateinit var tipAmountTextView: TextView
    private lateinit var totalAmountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        costEdittext = findViewById(R.id.costeditText)
        tipPrecentageEditText = findViewById(R.id.TippercentageeditText)
        serviceQualiyBar = findViewById(R.id.serviceratingBar)
        foodQualityBar = findViewById(R.id.foodratingBar)
        calculateButton = findViewById(R.id.button)
        tipAmountTextView = findViewById(R.id.valueofthetiptextView)
        totalAmountTextView = findViewById(R.id.costofalltextView)

        calculateButton.setOnClickListener{
            calculateTipAndTotal()
        }
    }

    private fun calculateTipAndTotal(){
        val cost = costEdittext.text.toString().toDoubleOrNull() ?: 0.0
        val tipPrecentage = tipPrecentageEditText.text.toString().toDoubleOrNull() ?: 0.0
        val serviceQuality = serviceQualiyBar.rating.toDouble()
        val foodQuality = foodQualityBar.rating.toDouble()

        val tipPercentageDecimal = tipPrecentage / 100

        var tipAmount = cost * tipPercentageDecimal
        if (serviceQuality <= 1.5 && serviceQuality != 0.0){
            tipAmount *= 0.9
        } else if (serviceQuality < 2.5 && serviceQuality > 1.5){
            tipAmount *= 0.95
        } else if (serviceQuality == 2.5 || serviceQuality == 0.0){
            tipAmount *= 1
        }
        else if (serviceQuality > 2.5 && serviceQuality < 4.5){
            tipAmount *= 1.05
        }
        else if (serviceQuality >= 4.5){
            tipAmount *= 1.1
        }

        if (foodQuality <= 1.5 && foodQuality != 0.0){
            tipAmount *= 0.9
        } else if (foodQuality < 2.5 && foodQuality > 1.5){
            tipAmount *= 0.95
        } else if (foodQuality == 2.5 || foodQuality == 0.0){
            tipAmount *= 1
        }
        else if (foodQuality > 2.5 && foodQuality < 4.5){
            tipAmount *= 1.05
        }
        else if (foodQuality >= 4.5){
            tipAmount *= 1.1
        }


        val totalAmount = cost + tipAmount

        tipAmountTextView.text = getString(R.string.tip_amount,tipAmount)
        totalAmountTextView.text = getString(R.string.total_amount,totalAmount)
    }

}

