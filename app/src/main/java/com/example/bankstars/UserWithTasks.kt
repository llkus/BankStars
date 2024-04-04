package com.example.bankstars

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithTasks(
    @Embedded
    val user:User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val task:List<Task>
)
