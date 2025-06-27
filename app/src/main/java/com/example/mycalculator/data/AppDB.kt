package com.example.mycalculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HistoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calculator_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}