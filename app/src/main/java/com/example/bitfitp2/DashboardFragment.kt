package com.example.bitfitp2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private lateinit var dao: NutritionEntryDao
    private lateinit var textViewAvgCalories: TextView
    private lateinit var textViewMaxCalories: TextView
    private lateinit var textViewMinCalories: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dao = MyApp.database.nutritionEntryDao()


        textViewAvgCalories = view.findViewById(R.id.textViewAvgCalories)
        textViewMaxCalories = view.findViewById(R.id.textViewMaxCalories)
        textViewMinCalories = view.findViewById(R.id.textViewMinCalories)

        lifecycleScope.launch {
            val calorieData = fetchCalorieDataFromDatabase()
            calculateAndDisplaySummary(calorieData)
        }

        return view
    }

    private suspend fun fetchCalorieDataFromDatabase(): IntArray {
        val entries = dao.getAllEntries()
        return entries.map { it.caloriesConsumed }.toIntArray()
    }

    @SuppressLint("SetTextI18n")
    private fun calculateAndDisplaySummary(calorieData: IntArray) {
        val totalCalories = calorieData.sum()
        val maxCalories = calorieData.maxOrNull() ?: 0
        val minCalories = calorieData.minOrNull() ?: 0
        val avgCalories = if (calorieData.isNotEmpty()) totalCalories / calorieData.size else 0

        textViewAvgCalories.text = "Average Calories: $avgCalories"
        textViewMaxCalories.text = "Max Calories: $maxCalories"
        textViewMinCalories.text = "Min Calories: $minCalories"
    }
}