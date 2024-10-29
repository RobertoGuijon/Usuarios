package com.robertoguijon.login.data.model

import com.robertoguijon.login.data.database.entities.PermisoEntity

data class Permiso(
    val idPermiso: Int,
    val permiso: String
)

fun PermisoEntity.toModel() = Permiso(idPermiso, permiso)