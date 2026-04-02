package com.example.habittracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,

    // total count of completions (can be used as "days done" for demo)
    val completedDays: Int = 0,

    // streak system
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastCompletedDate: Long = 0L,

    // reminder time
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0
)
