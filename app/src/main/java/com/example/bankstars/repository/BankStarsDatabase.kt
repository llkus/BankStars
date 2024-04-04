package com.example.bankstars.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bankstars.Task

import com.example.bankstars.User





    @Database(entities = [User::class, Task::class], version = 3)
    abstract class BankStarsDatabase : RoomDatabase() {
        abstract fun getUserDAO(): UserDAO
        abstract fun getTaskDAO(): TaskDAO
    }
