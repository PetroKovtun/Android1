package com.example.habittracker

class HabitRepository(private val habitDao: HabitDao) {

    val allHabits = habitDao.getAllHabits()

    suspend fun insert(habit: Habit): Long = habitDao.insert(habit)
    suspend fun update(habit: Habit) = habitDao.update(habit)
    suspend fun delete(habit: Habit) = habitDao.delete(habit)
}
