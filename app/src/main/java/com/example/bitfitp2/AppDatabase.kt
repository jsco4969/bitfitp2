package com.example.bitfitp2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NutritionEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nutritionEntryDao(): NutritionEntryDao
}