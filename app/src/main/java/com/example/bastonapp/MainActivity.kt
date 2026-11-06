package com.example.bastonapp

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvHistorial: TextView
    private lateinit var adminBD: AdminSQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHistorial = findViewById(R.id.tvHistorial)
        val btnSimular = findViewById<Button>(R.id.btnSimularInactividad)
        adminBD = AdminSQLiteOpenHelper(this)

        // Cargar las alertas al iniciar
        actualizarHistorial()

        // Evento del Botón: Simula la recepción de la alerta desde el Arduino
        btnSimular.setOnClickListener {
            guardarAlertaInactividad()
        }
    }

    private fun guardarAlertaInactividad() {
        val bd = adminBD.writableDatabase

        // Obtener la fecha y hora actual formateada
        val fechaHoraActual = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        val registro = ContentValues().apply {
            put("tipo_alerta", "Inactividad prolongada detectada")
            put("fecha_hora", fechaHoraActual)
            put("duracion_minutos", 5) // Los 5 minutos sin movimiento
        }

        val resultado = bd.insert("alertas", null, registro)
        bd.close()

        if (resultado != -1L) {
            Toast.makeText(this, "🚨 ¡Alerta de 5 min registrada en SQLite!", Toast.LENGTH_LONG).show()
            actualizarHistorial()
        } else {
            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarHistorial() {
        val bd = adminBD.readableDatabase
        val cursor = bd.rawQuery("SELECT fecha_hora, tipo_alerta, duracion_minutos FROM alertas ORDER BY id DESC", null)

        val stringBuilder = StringBuilder()

        if (cursor.moveToFirst()) {
            do {
                val fecha = cursor.getString(0)
                val tipo = cursor.getString(1)
                val mins = cursor.getInt(2)
                stringBuilder.append("• [$fecha]\n  $tipo ($mins min sin movimiento)\n\n")
            } while (cursor.moveToNext())
        } else {
            stringBuilder.append("No hay alertas registradas aún.")
        }

        cursor.close()
        bd.close()

        tvHistorial.text = stringBuilder.toString()
    }
}