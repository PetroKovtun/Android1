package com.example.habittracker

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class AddHabitActivity : AppCompatActivity() {

    private val viewModel: HabitViewModel by viewModels()

    private var pickedHour: Int = 20
    private var pickedMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etDesc = findViewById<TextInputEditText>(R.id.etDesc)
        val btnPickTime = findViewById<MaterialButton>(R.id.btnPickTime)
        val btnSave = findViewById<MaterialButton>(R.id.btnSave)

        btnPickTime.text = "Вибрати час: %02d:%02d".format(pickedHour, pickedMinute)

        btnPickTime.setOnClickListener {
            val now = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    pickedHour = hourOfDay
                    pickedMinute = minute
                    btnPickTime.text = "Вибрати час: %02d:%02d".format(pickedHour, pickedMinute)
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()
        }

        btnSave.setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            val desc = etDesc.text?.toString()?.trim().orEmpty()

            if (name.isBlank()) {
                Toast.makeText(this, "Введи назву", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val habit = Habit(
                name = name,
                description = desc,
                reminderHour = pickedHour,
                reminderMinute = pickedMinute
            )

            // Insert -> get real ID -> schedule alarm with that ID
            viewModel.insert(habit) { newId ->
                val saved = habit.copy(id = newId.toInt())
                NotificationHelper.scheduleDailyReminder(this, saved)
            }

            finish()
        }
    }
}
