package com.example.habittracker

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatisticsActivity : AppCompatActivity() {

    private val viewModel: HabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val barChart = findViewById<BarChart>(R.id.barChart)

        viewModel.allHabits.observe(this) { habits ->
            val labels = habits.map { it.name.take(10) } // короткі підписи
            val entries = habits.mapIndexed { index, habit ->
                BarEntry(index.toFloat(), habit.completedDays.toFloat())
            }

            val dataSet = BarDataSet(entries, "Виконано (разів/днів)")
            dataSet.valueTextSize = 12f

            val data = BarData(dataSet)
            data.barWidth = 0.8f

            barChart.data = data
            barChart.setFitBars(true)
            barChart.description.isEnabled = false
            barChart.axisRight.isEnabled = false

            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)

            barChart.axisLeft.axisMinimum = 0f

            barChart.animateY(900)
            barChart.invalidate()
        }
    }
}
