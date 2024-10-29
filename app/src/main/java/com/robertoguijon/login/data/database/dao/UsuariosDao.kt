package com.robertoguijon.login.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robertoguijon.login.data.database.entities.UsuarioEntity
import com.robertoguijon.login.data.model.UsuarioConImagen

@Dao
interface UsuariosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuarioEntity: UsuarioEntity) : Long

    @Query("SELECT idUsuario FROM usuarios WHERE (usuario = :usuario or correo = :usuario) AND password = :password")
    suspend fun login(usuario: String, password: String): Int?

    @Query("SELECT nombre FROM usuarios WHERE idUsuario = :idUsuario")
    suspend fun getNombre(idUsuario: Int): String

    @Query("SELECT password FROM usuarios WHERE idUsuario = :idUsuario LIMIT 1")
    suspend fun getPassword(idUsuario: Int): String

    @Query("SELECT idPermiso FROM usuarios WHERE idUsuario = :idUsuario")
    suspend fun getPermiso(idUsuario: Int): Int

    @Query("SELECT idImagen FROM usuarios WHERE idUsuario = :idUsuario")
    suspend fun getIdImagen(idUsuario: Int): Int

    @Query("SELECT u.*, img.imagen FROM usuarios u INNER JOIN imagenes img ON img.idImagen = u.idImagen WHERE u.idUsuario = :idUsuario")
    suspend fun getUsuario(idUsuario: Int): UsuarioConImagen

    @Query("SELECT u.*, img.imagen FROM usuarios u INNER JOIN imagenes img ON img.idImagen = u.idImagen WHERE u.usuario LIKE '%' || :valor || '%' OR u.correo LIKE '%' || :valor || '%' OR u.nombre LIKE '%' || :valor || '%'")
    suspend fun findUsuariosBy(valor: String): List<UsuarioConImagen>

    @Query("UPDATE usuarios SET password = :password WHERE idUsuario = :idUsuario")
    suspend fun updatePassword(password: String, idUsuario: Int) : Int

    @Query("UPDATE usuarios SET idImagen = :idImagen WHERE idUsuario = :idUsuario")
    suspend fun updateIdImagen(idImagen: Int, idUsuario: Int) : Int

    @Query("UPDATE usuarios SET idPermiso = :idPermiso WHERE idUsuario = :idUsuario")
    suspend fun updatePermiso(idPermiso: Int, idUsuario: Int) : Int

    @Query("UPDATE usuarios SET idImagen = :idImagen WHERE idUsuario = :idUsuario")
    suspend fun updateImagen(idImagen: Int, idUsuario: Int) : Int

    @Query("DELETE FROM usuarios WHERE idUsuario = :idUsuario")
    suspend fun deleteUser(idUsuario: Int) : Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :usuario LIMIT 1")
    suspend fun searchNameUser(usuario: String): Int

    @Query("SELECT u.*, img.imagen FROM usuarios u INNER JOIN imagenes img ON img.idImagen = u.idImagen")
    fun getUsuariosConImagen(): List<UsuarioConImagen>

    @Query("SELECT COUNT(*) FROM usuarios WHERE correo = :correo LIMIT 1")
    fun searchEmail(correo: String): Int

}