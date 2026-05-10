package com.senin.taskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senin.taskmanager.data.Task
import com.senin.taskmanager.data.TaskPriority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onAddTask: () -> Unit, viewModel: TaskViewModel = viewModel()) {
    val tasks by viewModel.todayTasks.collectAsState(initial = emptyList())
    val important = tasks.filter { it.priority == TaskPriority.IMPORTANT }
    val other = tasks.filter { it.priority == TaskPriority.OTHER }

    Scaffold(
        topBar = { TopAppBar(title = { Text("📋 Bugünün Görevleri") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, "Ekle")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (important.isNotEmpty()) {
                item { SectionHeader("⭐ Önemli") }
                items(important) { TaskCard(it, viewModel) }
            }
            if (other.isNotEmpty()) {
                item { SectionHeader("📌 Diğer") }
                items(other) { TaskCard(it, viewModel) }
            }
            if (tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🎉 Bugün yapılacak görev yok!", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun TaskCard(task: Task, viewModel: TaskViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = { if (it) viewModel.completeAndReschedule(task) }
            )
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    freqLabel(task.frequency.name),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            IconButton(onClick = { viewModel.deleteTask(task) }) {
                Icon(Icons.Default.Delete, "Sil", tint = Color.Red)
            }
        }
    }
}

fun freqLabel(name: String) = when (name) {
    "DAILY"        -> "Her gün"
    "EVERY_2_DAYS" -> "2 günde bir"
    "EVERY_3_DAYS" -> "3 günde bir"
    "WEEKLY"       -> "Haftada bir"
    else           -> ""
}
