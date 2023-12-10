package com.devapps.pamwezi.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index


@Entity(tableName = "budget_expense",
    foreignKeys = [
    ForeignKey(
        entity = BudgetLocal::class,
        parentColumns = ["id"],
        childColumns = ["budget_id"]
    )
],
    indices = [Index("budget_id")]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int = 0,
    @ColumnInfo("expense_title")
    val expenseTitle: String,
    @ColumnInfo("quantity")
    val qty: Int,
    @ColumnInfo("price")
    val price: Double,
    @ColumnInfo("budget_id")
    val id: Int
)
