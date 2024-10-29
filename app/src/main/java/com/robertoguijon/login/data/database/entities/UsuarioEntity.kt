package com.robertoguijon.login.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robertoguijon.login.data.model.Usuario

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val idUsuario: Int = 0,
    val nombre: String,
    val correo: String,
    val usuario: String,
    val password: String,
    val idPermiso: Int,
    val idImagen: Int
)

fun Usuario.toEntity() = UsuarioEntity(
    nombre = nombre,
    correo = correo,
    usuario = usuario,
    password = password,
    idPermiso = idPermiso,
    idImagen = idImagen
)