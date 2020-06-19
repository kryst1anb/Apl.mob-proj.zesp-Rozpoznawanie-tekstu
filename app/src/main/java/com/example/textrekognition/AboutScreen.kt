package com.example.textrekognition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat

class AboutScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_screen)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.myToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "About application";
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
        val intent = Intent(this,PickPhotoActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun aboutActivity(){
        Toast.makeText(this, "You are in this option!", Toast.LENGTH_SHORT).show()
    }
}