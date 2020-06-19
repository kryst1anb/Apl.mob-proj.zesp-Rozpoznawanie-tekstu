package com.example.textrekognition

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class TakePhotoActivity : AppCompatActivity() {
    lateinit var captureImageBtn: Button
    lateinit var detectTextBtn: Button
    lateinit var imageView: ImageView
    lateinit var textView: TextView
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var imageBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Take photo";


        captureImageBtn = findViewById(R.id.capture_image_btn)
        detectTextBtn = findViewById(R.id.detect_text_image_btn)
        imageView = findViewById(R.id.image_view)
        textView = findViewById(R.id.text_display)

        captureImageBtn.setOnClickListener{
            dispatchTakePictureIntent()
            textView.text = ""
        }

        detectTextBtn.setOnClickListener {
            detectTextFromImage()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.take_photo -> takePhotoActivity();
            R.id.choose_photo -> pickPhotoActivity();
            R.id.about -> aboutActivity();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun takePhotoActivity(){
        Toast.makeText(this, "You are in this option!", Toast.LENGTH_SHORT).show()
    }

    private fun pickPhotoActivity(){
        val intent = Intent(this,PickPhotoActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun aboutActivity(){
        val intent = Intent(this,AboutScreen::class.java)
        ContextCompat.startActivity(this, intent, null)
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun detectTextFromImage() {

        val image = FirebaseVisionImage.fromBitmap(imageBitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val result = detector.processImage(image)
        result.addOnSuccessListener { firebaseVisionText ->
            displayTextFromImage(firebaseVisionText)

        }
            .addOnFailureListener {
                Toast.makeText(this, "No text to detect", Toast.LENGTH_LONG).show()
            }
    }

    private fun displayTextFromImage(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            textView.setText("No Text Found")
            return
        }
        for (block in resultText.textBlocks) {
            val blockText = block.text
            textView.append(blockText + "\n")
        }
    }
}