package com.senin.taskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.senin.taskmanager.data.TaskFrequency
import com.senin.taskmanager.data.TaskPriority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(onBack: () -> Unit, viewModel: TaskViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var desc  by remember { mutableStateOf("") }
    var freq  by remember { mutableStateOf(TaskFrequency.DAILY) }
    var prio  by remember { mutableStateOf(TaskPriority.IMPORTANT) }

    Scaffold(topBar = { TopAppBar(title = { Text("Yeni Görev") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Görev adı") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("Açıklama (opsiyonel)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tekrar Sıklığı", style = MaterialTheme.typography.titleSmall)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = freq == TaskFrequency.DAILY,
                        onClick = { freq = TaskFrequency.DAILY },
                        label = { Text("Günlük") }
                    )
                    FilterChip(
                        selected = freq == TaskFrequency.EVERY_2_DAYS,
                        onClick = { freq = TaskFrequency.EVERY_2_DAYS },
                        label = { Text("2 Günlük") }
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = freq == TaskFrequency.EVERY_3_DAYS,
                        onClick = { freq = TaskFrequency.EVERY_3_DAYS },
                        label = { Text("3 Günlük") }
                    )
                    FilterChip(
                        selected = freq == TaskFrequency.WEEKLY,
                        onClick = { freq = TaskFrequency.WEEKLY },
                        label = { Text("Haftalık") }
                    )
                }
            }

            Text("Öncelik", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = prio == TaskPriority.IMPORTANT,
                    onClick = { prio = TaskPriority.IMPORTANT },
                    label = { Text("⭐ Önemli") }
                )
                FilterChip(
                    selected = prio == TaskPriority.OTHER,
                    onClick = { prio = TaskPriority.OTHER },
                    label = { Text("📌 Diğer") }
                )
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addTask(title, desc, freq, prio)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Kaydet") }
        }
    }
}
