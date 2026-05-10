package com.senin.taskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TaskFrequency { DAILY, EVERY_2_DAYS, EVERY_3_DAYS, WEEKLY }
enum class TaskPriority { IMPORTANT, OTHER }

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val frequency: TaskFrequency,
    val priority: TaskPriority,
    val nextDueDate: String = java.time.LocalDate.now().toString(),
    val isCompleted: Boolean = false
)
