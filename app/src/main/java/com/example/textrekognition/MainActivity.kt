package com.example.textrekognition


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toolbar
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val btnNext:Button = findViewById(R.id.button_next)
            btnNext.setOnClickListener {
                takePhotoActivity();
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
        val intent = Intent(this,PickPhotoActivity::class.java)
        ContextCompat.startActivity(this, intent, null)
    }

    private fun aboutActivity(){
        val intent = Intent(this,AboutScreen::class.java)
        ContextCompat.startActivity(this, intent, null)
    }
}

