package com.example.lostpets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLista(
    navegarDetalle: (Int) -> Unit,
    navegarCrear: () -> Unit,
    onLogout: () -> Unit
) {
    // Estados para el buscador y el filtro de usuario
    var textoBusqueda by remember { mutableStateOf("") }
    var mostrarSoloMios by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mascotas Perdidas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onLogout() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cerrar Sesión")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navegarCrear() },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir", tint = MaterialTheme.colorScheme.onSecondary)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            // Campo de Búsqueda
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                placeholder = { Text("Buscar nombre o zona...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            // Filtro rápido "Mis Publicaciones"
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mis publicaciones", modifier = Modifier.weight(1f))
                Switch(
                    checked = mostrarSoloMios,
                    onCheckedChange = { mostrarSoloMios = it }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // filtrado de buscador y filtro de dueño
            val listaFiltrada = listaMascotasPrueba.filter { mascota ->
                val coincideTexto = textoBusqueda.isEmpty() ||
                        mascota.nombre.contains(textoBusqueda, ignoreCase = true) ||
                        mascota.ubicacion.contains(textoBusqueda, ignoreCase = true)

                val coincideUsuario = !mostrarSoloMios || (mascota.creador == usuarioActual)

                coincideTexto && coincideUsuario
            }

            // LazyColum para optimizacion
            LazyColumn {
                items(listaFiltrada) { mascota ->
                    ItemMascota(
                        mascota = mascota,
                        onMascotaClick = { id -> navegarDetalle(id) }
                    )
                }
            }
        }
    }
}