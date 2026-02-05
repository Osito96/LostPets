package com.example.lostpets

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    idMascota: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current // Necesario para lanzar Intents (Maps, Llamadas)

    // Buscamos la mascota. Si se ha borrado, salimos para evitar errores.
    val index = listaMascotasPrueba.indexOfFirst { it.id == idMascota }
    if (index == -1) {
        onBack()
        return
    }

    val mascota = listaMascotasPrueba[index]

    // Comprobamos si el que ve el anuncio es el dueño
    val esMio = mascota.creador == usuarioActual

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(mascota.nombre, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Si es mío, muestro botón de BORRAR
                    if (esMio) {
                        IconButton(onClick = {
                            listaMascotasPrueba.remove(mascota) // CRUD: Delete
                            onBack()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                        }
                    }
                    // Botón para generar PDF del cartel
                    IconButton(onClick = {
                        GeneradorPDF.generarCartel(context, mascota)
                    }) {
                        Icon(Icons.Default.Print, contentDescription = "Imprimir Cartel")
                    }
                }
            )
        },
        floatingActionButton = {
            if (esMio) {
                // Si es mío, Botón para cambiar estado (Perdido/Encontrado)
                ExtendedFloatingActionButton(
                    onClick = {
                        val nuevoEstado = !mascota.esPerdido
                        val nuevoColor = if (nuevoEstado) Color(0xFFFF5252) else Color(0xFF4CAF50)

                        // Actualizamos el objeto en la lista
                        listaMascotasPrueba[index] = mascota.copy(
                            esPerdido = nuevoEstado,
                            colorEstado = nuevoColor
                        )
                    },
                    icon = { Icon(Icons.Default.Edit, "Editar") },
                    text = { Text(if (mascota.esPerdido) "Marcar Encontrado" else "Marcar Perdido") },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            } else {
                // Si NO es mío, Botón para LLAMAR (Intent Teléfono)
                ExtendedFloatingActionButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${mascota.telefonoContacto}")
                        }
                        context.startActivity(intent)
                    },
                    icon = { Icon(Icons.Default.Call, "Llamar") },
                    text = { Text("Contactar") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(mascota.imagenUri).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {

                Text("Publicado por: ${mascota.creador}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))

                SuggestionChip(
                    onClick = { },
                    label = { Text(if (mascota.esPerdido) "SE BUSCA" else "ENCONTRADO") },
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = mascota.colorEstado, labelColor = Color.White)
                )

                Text(mascota.nombre, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text(mascota.raza, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                Text("Información", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(mascota.descripcion, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))

                // Fila clicable para abrir GOOGLE MAPS
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("geo:0,0?q=${mascota.ubicacion}")
                            }
                            try { context.startActivity(intent) } catch (e: Exception) { }
                        }
                        .padding(8.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Abrir mapa", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Visto en: ${mascota.ubicacion}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("(Toca para ver en Mapa)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}