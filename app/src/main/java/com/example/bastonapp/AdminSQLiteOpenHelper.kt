package com.example.bastonapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(
    context: Context,
    name: String = "BastonBD",
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = 1
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Tabla de Usuarios (para el Login)
        db.execSQL(
            "CREATE TABLE usuarios (" +
                    "correo TEXT PRIMARY KEY, " +
                    "password TEXT, " +
                    "nombre TEXT)"
        )

        // 2. Tabla de Alertas (para guardar la inactividad de +5 min)
        db.execSQL(
            "CREATE TABLE alertas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tipo_alerta TEXT, " +
                    "fecha_hora TEXT, " +
                    "duracion_minutos INT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS alertas")
        onCreate(db)
    }
}