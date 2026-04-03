package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etFlower = findViewById<EditText>(R.id.etFlower)
        val rgColor = findViewById<RadioGroup>(R.id.rgColor)
        val rgPrice = findViewById<RadioGroup>(R.id.rgPrice)
        val btnOk = findViewById<Button>(R.id.btnOk)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnOk.setOnClickListener {
            val flowerText = etFlower.text.toString().trim().ifEmpty { "не вказано" }

            val color = when (rgColor.checkedRadioButtonId) {
                R.id.rbRed -> "червоний"
                R.id.rbWhite -> "білий"
                R.id.rbYellow -> "жовтий"
                -1 -> "не вибрано"
                else -> "не вибрано"
            }

            val price = when (rgPrice.checkedRadioButtonId) {
                R.id.rbLow -> "до 300 грн"
                R.id.rbMid -> "300–700 грн"
                R.id.rbHigh -> "від 700 грн"
                -1 -> "не вибрано"
                else -> "не вибрано"
            }

            tvResult.text = "Замовлення:\nКвіти: $flowerText\nКолір: $color\nЦіна: $price"
        }
    }
}
