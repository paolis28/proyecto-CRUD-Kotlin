package com.example.finalahorasi.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.pruebacrud.ui.theme.PruebaCrudTheme
import com.example.finalahorasi.retrofit.LoginResponse
import com.example.finalahorasi.retrofit.LoginRequest
import com.example.finalahorasi.viewmodels.LoginScreen
import com.example.finalahorasi.retrofit.Client
import androidx.navigation.compose.rememberNavController
import com.example.finalahorasi.model.NavGraph
import com.example.finalahorasi.model.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PruebaCrudTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController) // ← Llama a NavGraph en lugar de LoginScreen
            }
        }
    }
}
