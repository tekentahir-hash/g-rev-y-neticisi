package com.senin.taskmanager.ui

import android.app.Application
import androidx.lifecycle.*
import com.senin.taskmanager.data.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = TaskDatabase.getDatabase(application).taskDao()
    val todayTasks = dao.getTodayTasks()

    fun addTask(title: String, desc: String, freq: TaskFrequency, prio: TaskPriority) {
        viewModelScope.launch {
            dao.insertTask(Task(title = title, description = desc, frequency = freq, priority = prio))
        }
    }

    fun completeAndReschedule(task: Task) {
        viewModelScope.launch {
            val next = when (task.frequency) {
                TaskFrequency.DAILY        -> LocalDate.now().plusDays(1)
                TaskFrequency.EVERY_2_DAYS -> LocalDate.now().plusDays(2)
                TaskFrequency.EVERY_3_DAYS -> LocalDate.now().plusDays(3)
                TaskFrequency.WEEKLY       -> LocalDate.now().plusDays(7)
            }
            dao.updateTask(task.copy(isCompleted = false, nextDueDate = next.toString()))
        }
    }

    fun rollover() {
        viewModelScope.launch {
            val tomorrow = LocalDate.now().plusDays(1).toString()
            dao.getOverdueTasks().forEach { dao.updateTask(it.copy(nextDueDate = tomorrow)) }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { dao.deleteTask(task) }
    }
}
