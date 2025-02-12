package com.example.finalahorasi.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finalahorasi.retrofit.Client
import com.example.finalahorasi.retrofit.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ModificarView() {
    var name by remember { mutableStateOf("") }
    var license by remember { mutableStateOf("") }
    var maternalSurname by remember { mutableStateOf("") }
    var paternalSurname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Modificar Estudiante", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = license,
            onValueChange = { license = it },
            label = { Text("ID del estudiante") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {
                if (license.trim().isNotEmpty()) {
                    try {
                        buscarEstudiante(license.trim(), context) { student ->
                            student?.let {
                                name = it.name ?: ""
                                maternalSurname = it.maternalSurname ?: ""
                                paternalSurname = it.paternalSurname ?: ""
                                email = it.email ?: ""
                                errorMessage = ""
                            } ?: run {
                                errorMessage = "Estudiante no encontrado"
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ERROR", "Error al buscar estudiante: ${e.message}")
                        errorMessage = "Error inesperado"
                    }

                } else {
                    errorMessage = "Ingrese un ID válido"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar Estudiante")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del Estudiante") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = maternalSurname,
            onValueChange = { maternalSurname = it },
            label = { Text("Apellido materno") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = paternalSurname,
            onValueChange = { paternalSurname = it },
            label = { Text("Apellido paterno") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {
                val trimmedLicense = license.trim()
                val trimmedName = name.trim()
                val trimmedMaternalSurname = maternalSurname.trim()
                val trimmedPaternalSurname = paternalSurname.trim()
                val trimmedEmail = email.trim()

                if (trimmedLicense.isNotEmpty() && trimmedName.isNotEmpty() && trimmedMaternalSurname.isNotEmpty() &&
                    trimmedPaternalSurname.isNotEmpty() && trimmedEmail.isNotEmpty()) {

                    actualizarEstudiante(
                        trimmedLicense, trimmedName, trimmedMaternalSurname,
                        trimmedPaternalSurname, trimmedEmail, context
                    ) {
                        errorMessage = "Actualización exitosa"
                    }
                } else {
                    errorMessage = "Todos los campos son obligatorios."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar")
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}


// Función para buscar estudiante por matrícula
fun buscarEstudiante(studentID: String, context: Context, onResult: (Student?) -> Unit) {
    Client.instance.getStudent(studentID).enqueue(object : Callback<Student> {
        override fun onResponse(call: Call<Student>, response: Response<Student>) {
            if (response.isSuccessful && response.body() != null) {
                onResult(response.body())
            } else {
                onResult(null)
            }
        }

        override fun onFailure(call: Call<Student>, t: Throwable) {
            Toast.makeText(context, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            onResult(null)
        }
    })
}

// Función para actualizar estudiante
fun actualizarEstudiante(
    studentID: String,
    name: String,
    maternalSurname: String,
    paternalSurname: String,
    email: String,
    context: Context,
    onSuccess: () -> Unit
) {
    val request = Student(studentID, name, maternalSurname, paternalSurname, email)

    Client.instance.updateStudent(studentID, request).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Actualización exitosa", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Error en la actualización: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, "Fallo la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
