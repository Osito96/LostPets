package com.example.lostpets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrear(
    onBack: () -> Unit
) {
    // Variables temporales del formulario
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var esPerdido by remember { mutableStateOf(true) }

    var imagenSeleccionadaUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para abrir la galería del móvil de forma segura
    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        imagenSeleccionadaUri = uri
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Publicar Mascota", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Scroll por si el teclado tapa campos
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Zona para tocar y subir foto
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        launcherGaleria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                if (imagenSeleccionadaUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(imagenSeleccionadaUri).build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(48.dp))
                            Text("Toca para añadir foto")
                        }
                    }
                }
            }

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = raza, onValueChange = { raza = it }, label = { Text("Raza") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = ubicacion, onValueChange = { ubicacion = it }, label = { Text("Ubicación") }, modifier = Modifier.fillMaxWidth())

            // Solo permitimos números en el teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = { if (it.all { char -> char.isDigit() }) telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Está perdida?", modifier = Modifier.weight(1f))
                Switch(checked = esPerdido, onCheckedChange = { esPerdido = it })
            }

            // Botón de guardar: Crea el objeto Mascota y lo mete en la lista
            Button(
                onClick = {
                    val nuevaMascota = Mascota(
                        id = Random.nextInt(100, 100000), // ID aleatorio
                        nombre = nombre,
                        raza = raza,
                        descripcion = descripcion,
                        fechaPerdida = "Hoy",
                        ubicacion = ubicacion,
                        telefonoContacto = telefono,
                        imagenUri = imagenSeleccionadaUri?.toString() ?: "https://via.placeholder.com/150",
                        esPerdido = esPerdido,
                        creador = usuarioActual, // Firmamos con el usuario actual
                        colorEstado = if (esPerdido) Color(0xFFFF5252) else Color(0xFF4CAF50)
                    )
                    listaMascotasPrueba.add(0, nuevaMascota) // Añadir al principio
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = nombre.isNotEmpty() && telefono.isNotEmpty() // Validación simple
            ) {
                Text("PUBLICAR")
            }
        }
    }
}