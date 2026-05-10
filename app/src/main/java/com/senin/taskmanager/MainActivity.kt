package com.senin.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.senin.taskmanager.ui.AddTaskScreen
import com.senin.taskmanager.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                NavHost(nav, startDestination = "home") {
                    composable("home") { HomeScreen(onAddTask = { nav.navigate("add") }) }
                    composable("add")  { AddTaskScreen(onBack = { nav.popBackStack() }) }
                }
            }
        }
    }
}
