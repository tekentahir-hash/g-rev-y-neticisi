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
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    TaskFrequency.DAILY        to "Günlük",
                    TaskFrequency.EVERY_2_DAYS to "2 Günlük",
                    TaskFrequency.EVERY_3_DAYS to "3 Günlük",
                    TaskFrequency.WEEKLY       to "Haftalık"
                ).forEach { (f, label) ->
                    FilterChip(selected = freq == f, onClick = { freq = f }, label = { Text(label) })
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
