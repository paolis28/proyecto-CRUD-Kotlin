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

import com.example.finalahorasi.retrofit.ServiceApi
import com.example.finalahorasi.retrofit.StudentRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AltaStudentView(onAltaClick: () -> Unit) {
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
        Text(text = "Registro de Estudiante", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = license,
            onValueChange = { license = it },
            label = { Text("ID del estudiante") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre del Estudiante") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = maternalSurname,
            onValueChange = { maternalSurname = it },
            label = { Text("Apellido materno") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = paternalSurname,
            onValueChange = { paternalSurname = it },
            label = { Text("Apellido paterno") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

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
                // Elimina espacios en blanco al inicio y final
                val trimmedLicense = license.trim()
                val trimmedName = name.trim()
                val trimmedMaternalSurname = maternalSurname.trim()
                val trimmedPaternalSurname = paternalSurname.trim()
                val trimmedEmail = email.trim()

                if (trimmedLicense.isNotEmpty()) {
                    AltaUser(trimmedLicense, trimmedName, trimmedMaternalSurname, trimmedPaternalSurname, trimmedEmail, context, onAltaClick)
                } else {
                    errorMessage = "Todos los campos son obligatorios."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }


        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

fun AltaUser(
    license: String,
    name: String,
    maternalSurname: String,
    paternalSurname: String,
    email: String,
    context: Context,
    onAltaClick: () -> Unit
) {
    val request = StudentRequest(license, name, maternalSurname, paternalSurname, email)

    // Llamar al servicio correctamente
    Client.instance.registerStudent(request).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                onAltaClick()
            } else {
                Toast.makeText(context, "Error en el registro: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Toast.makeText(context, "Fallo la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

