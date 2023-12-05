package com.devapps.pamwezi.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_budget")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "budget_title")
    val title: String,
    @ColumnInfo(name = "budget_amount")
    val amount: Int,
    @ColumnInfo(name = "month")
    val month: String,
    @ColumnInfo(name = "created_by")
    val createdBy: String
)
