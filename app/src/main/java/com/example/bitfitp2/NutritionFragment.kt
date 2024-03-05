package com.example.bitfitp2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class NutritionFragment : Fragment() {

    private lateinit var viewModel: NutritionViewModel
    private lateinit var adapter: NutritionEntryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nutrition, container, false)



        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val addButton: Button = view.findViewById(R.id.addButton)
        val mealTypeEditText: EditText = view.findViewById(R.id.mealTypeEditText)
        val caloriesEditText: EditText = view.findViewById(R.id.caloriesEditText)

        // Set up ViewModel and RecyclerView
        val dao = MyApp.database.nutritionEntryDao()
        val repository = NutritionRepository(dao)
        viewModel = ViewModelProvider(this, NutritionViewModelFactory(repository))[NutritionViewModel::class.java]

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set LinearLayoutManager
        adapter = NutritionEntryAdapter()
        recyclerView.adapter = adapter

        // Add button click listener
        addButton.setOnClickListener {
            val mealType = mealTypeEditText.text.toString()
            val caloriesStr = caloriesEditText.text.toString()

            if (mealType.isNotBlank() && caloriesStr.isNotBlank()) {
                try {
                    val calories = caloriesStr.toInt()
                    val entry = NutritionEntry(
                        date = System.currentTimeMillis(),
                        mealType = mealType,
                        caloriesConsumed = calories
                    )
                    viewModel.insertEntry(entry)

                    // Clear input fields after successful addition
                    mealTypeEditText.text.clear()
                    caloriesEditText.text.clear()

                } catch (e: NumberFormatException) {
                    // Handle non-numeric input for calories
                    // Show an error message to the user
                    Toast.makeText(requireContext(), "Invalid calories value", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show an error message if any field is empty
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }


        // Observe LiveData to update RecyclerView
        lifecycleScope.launch {
            viewModel.getAllEntries().observe(viewLifecycleOwner) { entries ->
                adapter.submitList(entries)
            }
        }

        return view
    }
}