package com.example.bastonapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        val adminBD = AdminSQLiteOpenHelper(this)

        btnRegistrar.setOnClickListener {
            val correo = etCorreo.text.toString()
            val pass = etPassword.text.toString()

            if (correo.isNotEmpty() && pass.isNotEmpty()) {
                val bd = adminBD.writableDatabase
                val registro = ContentValues().apply {
                    put("correo", correo)
                    put("password", pass)
                    put("nombre", "Usuario Tutor")
                }
                bd.insert("usuarios", null, registro)
                bd.close()
                Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString()
            val pass = etPassword.text.toString()

            val bd = adminBD.readableDatabase
            val fila = bd.rawQuery(
                "SELECT correo FROM usuarios WHERE correo='$correo' AND password='$pass'",
                null
            )

            if (fila.moveToFirst()) {
                bd.close()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                bd.close()
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}