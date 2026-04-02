package com.example.habittracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val viewModel: HabitViewModel by viewModels()

    private val requestNotifPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askNotificationPermissionIfNeeded()

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val btnStats = findViewById<MaterialButton>(R.id.btnStats)

        val adapter = HabitAdapter(
            onDone = { habit ->
                val updated = applyStreakAndCompletion(habit)
                viewModel.update(updated)
            },
            onDelete = { habit ->

                NotificationHelper.cancelReminder(this, habit.id)
                viewModel.delete(habit)
            }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        viewModel.allHabits.observe(this) { list ->
            adapter.submitList(list)
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddHabitActivity::class.java))
        }

        btnStats.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
    }

    private fun applyStreakAndCompletion(habit: Habit): Habit {
        val now = System.currentTimeMillis()

        val dayMs = 24 * 60 * 60 * 1000L
        val todayIndex = now / dayMs
        val lastIndex = if (habit.lastCompletedDate == 0L) -1 else habit.lastCompletedDate / dayMs

        val newStreak = when {
            lastIndex == todayIndex -> habit.currentStreak // already done today (don’t double count streak)
            lastIndex == todayIndex - 1 -> habit.currentStreak + 1 // continued streak
            else -> 1
        }

        val best = maxOf(habit.bestStreak, newStreak)


        val newCompletedDays = if (lastIndex == todayIndex) habit.completedDays else habit.completedDays + 1
        val newLastCompleted = if (lastIndex == todayIndex) habit.lastCompletedDate else now

        return habit.copy(
            completedDays = newCompletedDays,
            currentStreak = newStreak,
            bestStreak = best,
            lastCompletedDate = newLastCompleted
        )
    }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
