package com.example.finalahorasi.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finalahorasi.retrofit.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EliminarView() {
    var studentID by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Eliminar Estudiante", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = studentID,
            onValueChange = { studentID = it },
            label = { Text("ID del estudiante") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val trimmedID = studentID.trim()
                if (trimmedID.isNotEmpty()) {
                    eliminarEstudiante(trimmedID, context) {
                        errorMessage = "Estudiante eliminado correctamente"
                        studentID = ""
                    }
                } else {
                    errorMessage = "Ingrese un ID válido"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar Estudiante")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

// Función para eliminar estudiante en el servidor
fun eliminarEstudiante(studentID: String, context: Context, onSuccess: () -> Unit) {
    Client.instance.deleteStudent(studentID).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Estudiante eliminado con éxito", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Error al eliminar: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, "Fallo la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

