package com.example.lostpets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lostpets.ui.theme.LostPetsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LostPetsTheme {
                NavegacionPrincipal()
            }
        }
    }
}

// Configuración de las rutas de la app
@Composable
fun NavegacionPrincipal() {
    val navController = rememberNavController()

    // Empezamos siempre en el Login
    NavHost(navController = navController, startDestination = "login") {

        // Pantalla 1: Login
        composable("login") {
            PantallaLogin(
                alEntrar = {
                    // Al entrar, borramos el login del historial para no volver atrás
                    navController.navigate("lista") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla 2: Lista Principal
        composable("lista") {
            PantallaLista(
                navegarDetalle = { id -> navController.navigate("detalle/$id") },
                navegarCrear = { navController.navigate("crear") },
                onLogout = {
                    // Al salir, volvemos al login
                    navController.navigate("login") {
                        popUpTo("lista") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla 3: Detalle
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            PantallaDetalle(idMascota = id, onBack = { navController.popBackStack() })
        }

        // Pantalla 4: Formulario de creación
        composable("crear") {
            PantallaCrear(onBack = { navController.popBackStack() })
        }
    }
}