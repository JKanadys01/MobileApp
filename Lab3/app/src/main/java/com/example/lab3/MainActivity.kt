package com.example.lab3

import android.content.Intent
import android.graphics.Bitmap
import android.media.Image.Plane
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var setingsButton: Button
    private lateinit var mapButton: Button
    private lateinit var cameraButton: Button
    private  lateinit var imageView: ImageView
    private lateinit var  latitudeText: EditText
    private  lateinit var  longitudeText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setingsButton = findViewById(R.id.display_setings_button)
        mapButton = findViewById(R.id.map_button)
        cameraButton = findViewById(R.id.camera_button)
        latitudeText = findViewById(R.id.latitudeeditTextText)
        longitudeText = findViewById(R.id.longitudeeditTextText)
        imageView = findViewById(R.id.imageView)
        setingsButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
            startActivity(intent)
        }

        mapButton.setOnClickListener {
            val latitude = latitudeText.text.toString()
            val longitude = longitudeText.text.toString()
            if (latitude.isEmpty() || longitude.isEmpty()) {
                Toast.makeText(this, "No Latitude and Longitude",Toast.LENGTH_LONG).show()
            }
            else {
                val uri = Uri.parse("geo:0,0?q=$latitude,$longitude")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }

        cameraButton.setOnClickListener {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    companion object {
        private const val  REQUEST_IMAGE_CAPTURE = 1
    }
}