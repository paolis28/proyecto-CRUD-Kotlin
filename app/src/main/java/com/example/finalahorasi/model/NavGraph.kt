package com.example.finalahorasi.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finalahorasi.viewmodels.AltaStudentView
import com.example.finalahorasi.viewmodels.BuscarView
import com.example.finalahorasi.viewmodels.EliminarView
import com.example.finalahorasi.viewmodels.LoginScreen
import com.example.finalahorasi.viewmodels.ModificarView
import com.example.finalahorasi.viewmodels.PanelView
import com.example.finalahorasi.viewmodels.TomarFoto

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen {
                navController.navigate("panel")
            }
        }
        
        composable("panel"){
            PanelView(navController)
        }

        composable("buscar_student"){
            BuscarView()
        }

        composable("alta_student") {
            AltaStudentView {
                navController.navigate("panel")
            }
        }

        composable("modificar_student") {
            ModificarView()
        }

        composable("eliminar_student") {
            EliminarView()
        }

        composable("tomar_foto") {
            TomarFoto()
        }
    }
}



