package com.example.lostpets

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Define qué información tiene cada animal
data class Mascota(
    val id: Int,
    val nombre: String,
    val raza: String,
    val descripcion: String,
    val fechaPerdida: String,
    val ubicacion: String,
    val telefonoContacto: String,
    val imagenUri: String?, // Guardamos la ruta de la imagen
    var esPerdido: Boolean = true, // Para poder cambiarlo de Perdido a Encontrado
    val creador: String, // Para saber quién es el dueño del anuncio
    val colorEstado: Color = Color(0xFFFF5252)
)

// Base de datos simulada en memoria
val listaMascotasPrueba = mutableStateListOf(
    Mascota(
        id = 1,
        nombre = "Toby",
        raza = "Golden Retriever",
        descripcion = "Tiene un collar rojo.",
        fechaPerdida = "12/05/2024",
        ubicacion = "Parque Central",
        telefonoContacto = "600111222",
        imagenUri = "https://images.unsplash.com/photo-1552053831-71594a27632d?q=80&w=600",
        esPerdido = true,
        creador = "Admin"
    ),
    Mascota(
        id = 2,
        nombre = "Luna",
        raza = "Gato Siamés",
        descripcion = "Ojos azules muy claros.",
        fechaPerdida = "10/05/2024",
        ubicacion = "Calle Mayor",
        telefonoContacto = "600333444",
        imagenUri = "https://images.unsplash.com/photo-1513245543132-31f507417b26?q=80&w=600",
        esPerdido = true,
        creador = "Admin"
    ),
    Mascota(
        id = 3,
        nombre = "Rex",
        raza = "Pastor Alemán",
        descripcion = "No tiene chip.",
        fechaPerdida = "13/05/2024",
        ubicacion = "Av. Constitución",
        telefonoContacto = "600555666",
        imagenUri = "https://images.unsplash.com/photo-1589941013453-ec89f33b5e95?q=80&w=600",
        esPerdido = false,
        creador = "Admin",
        colorEstado = Color(0xFF4CAF50)
    )
)

// Variable global para recordar quién ha iniciado sesión
var usuarioActual by mutableStateOf("")