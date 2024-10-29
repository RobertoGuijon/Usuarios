package com.robertoguijon.login.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.robertoguijon.login.data.model.Permiso

@Entity(tableName = "permisos")
data class PermisoEntity(
    @PrimaryKey(autoGenerate = true) val idPermiso: Int = 0,
    val permiso: String
)

fun Permiso.toEntity() = PermisoEntity(idPermiso, permiso)

