package com.robertoguijon.login.data.model

data class UsuarioConImagen(
    val idUsuario: Int,
    val nombre: String,
    val correo: String,
    val usuario: String,
    val password: String,
    val idPermiso: Int,
    val idImagen: Int,
    val imagen: ByteArray
)
