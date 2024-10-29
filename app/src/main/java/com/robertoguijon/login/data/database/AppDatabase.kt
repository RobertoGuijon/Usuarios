package com.robertoguijon.login.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robertoguijon.login.data.database.dao.ImagenesDao
import com.robertoguijon.login.data.database.dao.PermisosDao
import com.robertoguijon.login.data.database.dao.UsuariosDao
import com.robertoguijon.login.data.database.entities.ImagenEntity
import com.robertoguijon.login.data.database.entities.PermisoEntity
import com.robertoguijon.login.data.database.entities.UsuarioEntity

// Se asignan las tablas a la base de datos y los DAOs que se van a utilizar
@Database(entities = [UsuarioEntity::class, PermisoEntity::class, ImagenEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuariosDao(): UsuariosDao
    abstract fun permisosDao(): PermisosDao
    abstract fun imagenesDao(): ImagenesDao

}