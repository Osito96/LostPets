package com.example.lostpets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lostpets.ui.theme.isDarkModeGlobal

@Composable
fun PantallaLogin(
    alEntrar: () -> Unit
) {
    var nombreUsuario by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo de la App
        Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "LOST PETS",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Tarjeta del formulario
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Bienvenido", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = {
                        nombreUsuario = it
                        error = false // Quitamos el error si escribe algo
                    },
                    label = { Text("Introduce tu nombre") },
                    isError = error,
                    singleLine = true
                )

                if (error) {
                    Text("El nombre es obligatorio", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (nombreUsuario.isNotBlank()) {
                            usuarioActual = nombreUsuario // Guardamos el usuario en memoria
                            alEntrar()
                        } else {
                            error = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ENTRAR")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Interruptor para cambiar el tema de toda la app
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Modo Oscuro", color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isDarkModeGlobal,
                onCheckedChange = { isDarkModeGlobal = it }
            )
        }
    }
}