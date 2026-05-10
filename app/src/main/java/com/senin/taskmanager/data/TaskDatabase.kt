package com.senin.taskmanager.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE nextDueDate <= :today ORDER BY priority ASC")
    fun getTodayTasks(today: String = java.time.LocalDate.now().toString()): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND nextDueDate < :today")
    suspend fun getOverdueTasks(today: String = java.time.LocalDate.now().toString()): List<Task>
}

class Converters {
    @TypeConverter fun fromFreq(v: String) = TaskFrequency.valueOf(v)
    @TypeConverter fun toFreq(f: TaskFrequency) = f.name
    @TypeConverter fun fromPrio(v: String) = TaskPriority.valueOf(v)
    @TypeConverter fun toPrio(p: TaskPriority) = p.name
}

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    companion object {
        @Volatile private var INSTANCE: TaskDatabase? = null
        fun getDatabase(context: Context) = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(context, TaskDatabase::class.java, "task_db")
                .build().also { INSTANCE = it }
        }
    }
}
