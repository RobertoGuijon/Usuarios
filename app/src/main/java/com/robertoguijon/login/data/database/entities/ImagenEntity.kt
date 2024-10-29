package com.robertoguijon.login.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robertoguijon.login.data.model.Imagen

@Entity(tableName = "imagenes")
data class ImagenEntity(
    @PrimaryKey(autoGenerate = true) val idImagen: Int = 0,
    val imagen: ByteArray
)

fun Imagen.toEntity() = ImagenEntity(idImagen, imagen)