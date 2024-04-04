package com.example.bankstars

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks"
)
data class Task(
    @PrimaryKey
    val id:Int? = null,
    val name:String,
    val description:String,
    @ColumnInfo(name = "isTransactionPlus")
    val isTransactionPlus:Boolean,
    @ColumnInfo(name = "isTransactionMinus")
    val isTransactionMinus:Boolean,
    @ColumnInfo(name = "user_id")
    val userId:Int
)