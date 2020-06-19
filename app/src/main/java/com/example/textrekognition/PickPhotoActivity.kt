package com.example.textrekognition

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_pickphoto.*

class PickPhotoActivity: AppCompatActivity() {
    companion object {
        //image pick code
        private const val IMAGE_PICK_CODE = 1000;
        //Permission code
        private const val PERMISSION_CODE = 1001;
    }

    private lateinit var imageBitmap: Bitmap;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickphoto)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Pick photo";

        //BUTTON CLICK
        img_pick_btn.setOnClickListener {
            //check runtime permission
            if (VERSION.SDK_INT >= VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.size >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image_view.setImageURI(data?.data)
            imageBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data?.data as Uri));
        }
        detectTextFromImage();
    }
    private fun detectTextFromImage() {
        //val firebasevisionimage:FirebaseVisionImage
        val image = FirebaseVisionImage.fromBitmap(imageBitmap)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val result = detector.processImage(image)
        result.addOnSuccessListener { firebaseVisionText ->
            // Task completed successfully
            // ...

            displayTextFromImage(firebaseVisionText)
        }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG)
            }

    }

    private fun displayTextFromImage(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            text_display.text = "No Text Found"
            return
        }
        for (block in resultText.textBlocks) {
            val blockText = block.text
            text_display.append(blockText + "\n")
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
        val intent = Intent(this,TakePhotoActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun pickPhotoActivity(){
        Toast.makeText(this, "You are in this option!", Toast.LENGTH_SHORT).show()
    }

    private fun aboutActivity(){
        val intent = Intent(this,AboutScreen::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

}