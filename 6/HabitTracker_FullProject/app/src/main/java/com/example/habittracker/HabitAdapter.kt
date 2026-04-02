package com.example.habittracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class HabitAdapter(
    private val onDone: (Habit) -> Unit,
    private val onDelete: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitVH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitVH(v)
    }

    override fun onBindViewHolder(holder: HabitVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HabitVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        private val tvDays: TextView = itemView.findViewById(R.id.tvDays)
        private val tvStreak: TextView = itemView.findViewById(R.id.tvStreak)
        private val tvReminder: TextView = itemView.findViewById(R.id.tvReminder)

        private val btnDone: MaterialButton = itemView.findViewById(R.id.btnDone)
        private val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete)

        fun bind(habit: Habit) {
            tvName.text = habit.name
            tvDesc.text = habit.description
            tvDays.text = "Виконано: ${habit.completedDays}"
            tvStreak.text = "🔥 Серія: ${habit.currentStreak}   🏆 Рекорд: ${habit.bestStreak}"
            tvReminder.text = "⏰ Нагадування: %02d:%02d".format(habit.reminderHour, habit.reminderMinute)

            btnDone.setOnClickListener { onDone(habit) }
            btnDelete.setOnClickListener { onDelete(habit) }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Habit, newItem: Habit) = oldItem == newItem
        }
    }
}
