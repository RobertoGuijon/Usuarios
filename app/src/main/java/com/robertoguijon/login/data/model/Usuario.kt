package com.robertoguijon.login.data.model

import com.robertoguijon.login.data.database.entities.UsuarioEntity

data class Usuario(
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val usuario: String,
    val password: String,
    val idPermiso: Int,
    val idImagen: Int,
)

fun UsuarioEntity.toModel() = Usuario(idUsuario, nombre, correo, usuario, password, idPermiso, idImagen)