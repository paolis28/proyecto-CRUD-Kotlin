package com.example.finalahorasi.viewmodels

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PanelView(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomButton("Buscar Alumno") { navController.navigate("buscar_student") }
        CustomButton("Alta Alumno") { navController.navigate("alta_student") }
        CustomButton("Modificar Alumno") { navController.navigate("modificar_student") }
        CustomButton("Eliminar Alumno") { navController.navigate("eliminar_student") }
        CustomButton("Tomar foto") { navController.navigate("tomar_foto") }
    }
}

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(100.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text)
    }
}
