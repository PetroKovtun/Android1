package com.example.habittracker

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HabitRepository
    val allHabits: LiveData<List<Habit>>

    init {
        val dao = AppDatabase.getDatabase(application).habitDao()
        repository = HabitRepository(dao)
        allHabits = repository.allHabits
    }

    fun insert(habit: Habit, onId: (Long) -> Unit = {}) = viewModelScope.launch {
        val id = repository.insert(habit)
        onId(id)
    }

    fun update(habit: Habit) = viewModelScope.launch {
        repository.update(habit)
    }

    fun delete(habit: Habit) = viewModelScope.launch {
        repository.delete(habit)
    }
}
