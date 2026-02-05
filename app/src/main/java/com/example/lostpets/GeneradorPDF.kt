package com.example.lostpets

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient

object GeneradorPDF {

    // Función que crea un WebView invisible, inyecta HTML y lanza el gestor de impresión de Android
    fun generarCartel(context: Context, mascota: Mascota) {
        val webView = WebView(context)

        val colorEstado = if (mascota.esPerdido) "#FF5252" else "#4CAF50"
        val textoEstado = if (mascota.esPerdido) "SE BUSCA" else "ENCONTRADO"

        // Diseño del cartel en HTML simple
        val htmlContent = """
            <html>
            <head>
                <style>
                    body { font-family: sans-serif; padding: 20px; text-align: center; border: 5px solid $colorEstado; }
                    h1 { font-size: 50px; color: $colorEstado; }
                    .foto { width: 100%; max-height: 400px; object-fit: cover; border-radius: 10px; margin: 20px 0; }
                    .telefono { font-size: 40px; font-weight: bold; background-color: $colorEstado; color: white; padding: 20px; border-radius: 10px; }
                </style>
            </head>
            <body>
                <h1>$textoEstado</h1>
                <h2>${mascota.nombre} - ${mascota.raza}</h2>
                <img src="${mascota.imagenUri}" class="foto"/>
                <p><strong>Descripción:</strong> ${mascota.descripcion}</p>
                <p><strong>Ubicación:</strong> ${mascota.ubicacion}</p>
                <div class="telefono">📞 ${mascota.telefonoContacto}</div>
            </body>
            </html>
        """.trimIndent()

        // Cuando el HTML termina de cargar, lanzamos la impresión
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                createWebPrintJob(context, view!!)
            }
        }

        webView.loadDataWithBaseURL(null, htmlContent, "text/HTML", "UTF-8", null)
    }

    private fun createWebPrintJob(context: Context, webView: WebView) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
        val printAdapter = webView.createPrintDocumentAdapter("Cartel_Mascota")

        printManager?.print("Cartel_LostPets", printAdapter, PrintAttributes.Builder().build())
    }
}