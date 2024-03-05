package com.example.bitfitp2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.menu_nutrition -> fragment = NutritionFragment()
                R.id.menu_dashboard -> fragment = DashboardFragment()
            }
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
            true
        }

        // Set default fragment
        val nutritionFragment = NutritionFragment()
        fragmentManager.beginTransaction().replace(R.id.fragment_container, nutritionFragment).commit()
    }
}