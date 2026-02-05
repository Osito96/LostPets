package com.example.lostpets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

// Componente visual que representa una sola tarjeta de mascota en la lista
// Se llama desde PantallaLista tantas veces como mascotas haya
@Composable
fun ItemMascota(
    mascota: Mascota,
    onMascotaClick: (Int) -> Unit // Avisamos al padre cuando tocan la tarjeta
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMascotaClick(mascota.id) }, // Al hacer clic, enviamos el ID
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Sombra para dar profundidad
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            // Carga de imagen asíncrona (Coil)
            // Se adapta tanto a URL de internet como a URI de la galería local
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mascota.imagenUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop, // Recorta la imagen para llenar el cuadrado
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Columna de textos a la derecha de la foto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mascota.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = mascota.raza, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))

                // Etiqueta visual de estado (Perdido/Encontrado)
                SuggestionChip(
                    onClick = { }, // No hace nada al clicar, es solo visual
                    label = { Text(if (mascota.esPerdido) "PERDIDO" else "ENCONTRADO") },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = mascota.colorEstado,
                        labelColor = Color.White
                    ),
                    border = null
                )
            }
        }
    }
}