package com.robertoguijon.login.data.model

import com.robertoguijon.login.data.database.entities.ImagenEntity

data class Imagen(
    val idImagen: Int,
    val imagen: ByteArray
)

fun ImagenEntity.toModel() = Imagen(idImagen, imagen)