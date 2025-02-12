package com.example.finalahorasi.viewmodels

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.finalahorasi.retrofit.Client
import com.example.finalahorasi.retrofit.Student
import com.example.finalahorasi.retrofit.StudentRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun BuscarView() {
    var studentID by remember { mutableStateOf("") }
    var studentData by remember { mutableStateOf<Student?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Búsqueda de Estudiante", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = studentID,
            onValueChange = { studentID = it },
            label = { Text("ID del estudiante") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (studentID.isNotEmpty()) {
                buscarEstudiante(studentID, { studentData = it }, { errorMessage = it })
            } else {
                errorMessage = "El ID no puede estar vacío"
            }
        }) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar los datos del estudiante si existen
        studentData?.let { student ->
            Text("Nombre: ${student.name} ${student.paternalSurname} ${student.maternalSurname}")
            Text("Matrícula: ${student.license}")
            Text("Correo: ${student.email}")
        }

        // Mostrar error si ocurre
        errorMessage?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}

fun buscarEstudiante(license: String, onSuccess: (Student) -> Unit, onError: (String) -> Unit) {
    val call = Client.instance.getStudent(license)

    call.enqueue(object : Callback<Student> {
        override fun onResponse(call: Call<Student>, response: Response<Student>) {
            if (response.isSuccessful) {
                response.body()?.let { onSuccess(it) } ?: onError("No se encontró el estudiante")
            } else {
                onError("Error en la búsqueda: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<Student>, t: Throwable) {
            Log.e("BuscarView", "Error de red", t)
            onError("Error de conexión con el servidor")
        }
    })
}


