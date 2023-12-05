package com.devapps.pamwezi.domain.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devapps.pamwezi.domain.model.Budget
import com.devapps.pamwezi.domain.model.Expense

@Database(entities = [Budget::class, Expense::class], version = 1, exportSchema = false)
public abstract class ExpenseTrackerDatabase : RoomDatabase() {

    abstract fun budgetDao(): BudgetDao

    companion object {

        @Volatile
        private var Instance: ExpenseTrackerDatabase? = null

        fun getDatabase(context: Context) : ExpenseTrackerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ExpenseTrackerDatabase::class.java,
                    "expense_tracker_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}