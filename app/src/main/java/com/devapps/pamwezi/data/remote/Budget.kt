package com.devapps.pamwezi.data.remote

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Budget(
    val title: String,
    val amount: Double,
    val month: String,
    val createdBy: String
)
