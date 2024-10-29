package com.robertoguijon.login.data.database.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.robertoguijon.login.data.database.entities.ImagenEntity

@Dao
interface ImagenesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagen(imagenEntity: ImagenEntity) : Long

    @Update
    suspend fun updateImagen(imagenEntity: ImagenEntity) : Int

    @Query("SELECT * FROM imagenes WHERE idImagen = :idImagen")
    suspend fun getImagen(idImagen: Int): ImagenEntity?
}