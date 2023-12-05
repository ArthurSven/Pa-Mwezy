package com.devapps.pamwezi.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity( tableName = "expense_table",
    foreignKeys = [
            ForeignKey(
                entity = Budget::class,
                parentColumns = ["id"],
                childColumns = ["budget_id"]
            )
    ])
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int = 0,
    @ColumnInfo("expense_name")
    val expenseName: String,
    @ColumnInfo("price")
    val price: Double,
    @ColumnInfo("quantity")
    val qty: Int,
    @ColumnInfo("budget_id")
    val budget: Budget
)
