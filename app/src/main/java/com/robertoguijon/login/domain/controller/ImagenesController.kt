package com.robertoguijon.login.domain.controller

import android.util.Log
import com.robertoguijon.login.data.database.dao.ImagenesDao
import com.robertoguijon.login.data.database.entities.ImagenEntity
import com.robertoguijon.login.data.database.entities.toEntity
import com.robertoguijon.login.data.model.Imagen
import com.robertoguijon.login.data.model.Response
import com.robertoguijon.login.data.model.toModel
import javax.inject.Inject

class ImagenesController @Inject constructor(
    private val imagenDao: ImagenesDao
) {

    //Funcion para agregar una nueva imagen, retorna respuesta y el id de la imagen
    suspend fun addImagen(imagen: Imagen): Pair<Response, Int> {
        val imagenEntity: ImagenEntity = imagen.toEntity()
        return try {
            val idImagen = imagenDao.insertImagen(imagenEntity)
            if (idImagen > 0) {
                Pair(Response(true, "Imagen actualizada exitosamente"), idImagen.toInt())
            } else {
                Pair(Response(false, "La imagen no pudo ser actualizada. Intentelo de nuevo"), 0)
            }
        } catch (e: Exception) {
            Log.e("Error de base de datos", "Error al insertar imagen: ${e.message}")
            Pair(Response(false, "Ocurrió un error al insertar la imagen. Intentelo de nuevo"), 0)
        }
    }

    //Funcion para actualizar una imagen
    suspend fun updateImagen(imagen: Imagen): Response {
        val imagenEntity = imagen.toEntity()
        return try {
            if (imagenDao.updateImagen(imagenEntity) > 0) {
                Response(true, "Imagen actualizada correctamente")
            } else {
                Response(false,"La imagen no pudo ser actualizada. Intentelo de nuevo")
            }
        } catch (e: Exception) {
            Log.e("Error de base de datos", "Error al insertar imagen: ${e.message}")
            Response(false,"Ocurrió un error al actualizar la imagen. Intentelo de nuevo")
        }
    }

}