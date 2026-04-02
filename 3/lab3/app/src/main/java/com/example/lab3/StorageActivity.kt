package com.example.lab3

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        val tvData = findViewById<TextView>(R.id.tvData)

        val data = try {
            openFileInput("flowers.txt").bufferedReader().readText()
        } catch (e: Exception) {
            ""
        }

        tvData.text =
            if (data.isBlank()) "Сховище порожнє"
            else data
    }
}
