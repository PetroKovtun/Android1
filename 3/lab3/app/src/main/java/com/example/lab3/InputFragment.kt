package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment

class InputFragment : Fragment(R.layout.fragment_input) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etFlower = view.findViewById<EditText>(R.id.etFlower)
        val rgColor = view.findViewById<RadioGroup>(R.id.rgColor)
        val rgPrice = view.findViewById<RadioGroup>(R.id.rgPrice)
        val btnOk = view.findViewById<Button>(R.id.btnOk)
        val btnOpen = view.findViewById<Button>(R.id.btnOpen)

        btnOk.setOnClickListener {
            val flower = etFlower.text.toString()

            val color = when (rgColor.checkedRadioButtonId) {
                R.id.rbRed -> "Червоний"
                R.id.rbWhite -> "Білий"
                R.id.rbYellow -> "Жовтий"
                else -> "не вибрано"
            }

            val price = when (rgPrice.checkedRadioButtonId) {
                R.id.rbLow -> "до 300 грн"
                R.id.rbMid -> "300–700 грн"
                R.id.rbHigh -> "від 700 грн"
                else -> "не вибрано"
            }

            val result =
                "Замовлення:\n" +
                        "Квіти: $flower\n" +
                        "Колір: $color\n" +
                        "Ціна: $price"

            saveToFile(result)

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ResultFragment.newInstance(result)
                )
                .addToBackStack(null)
                .commit()
        }

        btnOpen.setOnClickListener {
            startActivity(Intent(requireContext(), StorageActivity::class.java))
        }
    }

    private fun saveToFile(data: String) {
        val file = requireContext()
            .openFileOutput("flowers.txt", android.content.Context.MODE_APPEND)
        file.write((data + "\n\n").toByteArray())
        file.close()

        Toast.makeText(requireContext(), "Дані збережено", Toast.LENGTH_SHORT).show()
    }
}
