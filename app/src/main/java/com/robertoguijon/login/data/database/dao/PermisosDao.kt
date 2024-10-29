package com.robertoguijon.login.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.robertoguijon.login.data.database.entities.PermisoEntity

@Dao
interface PermisosDao {

    @Query("SELECT * FROM permisos WHERE idPermiso = :idPermiso")
    suspend fun getPermiso(idPermiso: Int): PermisoEntity?

}