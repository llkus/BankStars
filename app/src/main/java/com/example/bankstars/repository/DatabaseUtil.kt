package com.example.bankstars.repository

import android.content.Context
import androidx.room.Room

object DatabaseUtil {
    private var instance: BankStarsDatabase? = null

    fun getInstance(context: Context): BankStarsDatabase {
        if(instance == null){
            instance = Room.databaseBuilder(
                context,
                BankStarsDatabase::class.java,
                "bankstars.db"
            )
                .fallbackToDestructiveMigration()
                .build()

        }
        return instance!!
    }
}