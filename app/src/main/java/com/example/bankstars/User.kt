package com.example.bankstars

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "users",
        indices = [Index(value = ["email"], unique = true)]
)
data class User (
        @PrimaryKey
        val id:Int? = null,
        val name:String,
        val email:String,
        val fone:String,
        val password:String
)
