package com.devapps.pamwezi.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_table")
data class BudgetLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("budget_title")
    val title: String,
    @ColumnInfo("budget_amount")
    val amount: Double,
    @ColumnInfo("month")
    val month: String,
    @ColumnInfo("createdBy")
    val createdBy: String
)
