package com.robertoguijon.login.domain.controller

import android.util.Log
import android.util.Patterns
import com.robertoguijon.login.LoginApp.Companion.idUserSP
import com.robertoguijon.login.data.database.dao.UsuariosDao
import com.robertoguijon.login.data.database.entities.toEntity
import com.robertoguijon.login.data.model.Response
import com.robertoguijon.login.data.model.Usuario
import com.robertoguijon.login.data.model.UsuarioConImagen
import javax.inject.Inject

class UsersController @Inject constructor(
    private val usuarioDao: UsuariosDao
) {

    //Funcion para registrar un usuario
    suspend fun registrarUsuario(usuario: Usuario, passwordD: String): Response {

        if(usuario.nombre.isEmpty() || usuario.usuario.isEmpty() || usuario.correo.isEmpty() || usuario.password.isEmpty()){
            return Response(false, "Los campos no pueden estar vacíos")
        }

        if(usuario.nombre.length < 10){
            return Response(false, "El nombre debe tener al menos 10 caracteres")
        }

        if(!validarNombre(usuario.nombre)){
            return Response(false, "El nombre no es válido")
        }

        if(usuario.usuario.length < 8){
            return Response(false, "El nombre de usuario debe tener al menos 8 caracteres")
        }

        if(usuarioDao.searchNameUser(usuario.usuario) > 0){
            return Response(false, "El nombre de usuario ya existe")
        }

        if(!isValidEmail(usuario.correo)){
            return Response(false, "El correo no es válido")
        }

        if(usuarioDao.searchEmail(usuario.correo) > 0){
            return Response(false, "El correo ya existe")
        }

        if(!coincidePassword(usuario.password, passwordD)){
            return Response(false, "Las contraseñas no coinciden")
        }

        if(usuario.password.length < 8){
            return Response(false, "La contraseña debe tener al menos 8 caracteres")
        }

        if(!isValidPassword(usuario.password)){
            return Response(false, "Formato de contraseña invalido")
        }

        return try {
            val idUser = usuarioDao.insertUsuario(usuario.toEntity())
            if (idUser > 0) {
                Response(true, "Usuario registrado correctamente")
            } else {
                Response(false, "No se pudo registrar el usuario. Intentelo de nuevo")
            }
        }catch (e: Exception) {
            Log.e("Error de base de datos", "Error al insertar usuario: ${e.message}")
            Response(false, "Ocurrió un error al registrar el usuario")
        }
    }

    //Funcion para iniciar sesion
    suspend fun login(usuario: String, password: String): Response {
        if(usuario.isEmpty() || password.isEmpty()){
            return Response(false, "Los campos no pueden estar vacíos")
        }

        return try {
            val idUser = usuarioDao.login(usuario, password)
            Log.d("idUsuario", "$idUser")
            if (idUser != null && idUser > 0) {
                //Almacenamos la id del usuario en SharedPreferences
                idUserSP.saveId(idUser)
                Response(true,"Se inició sesión correctamente")
            } else {
                Response(false,"Credenciales incorrectas")
            }
        } catch (e: Exception) {
            Log.e("UserController", "Error al iniciar sesión", e)
            Response(false,"Error al iniciar sesión")
        }
    }

    //Funcion para obtener el usuario con su correspondiente imagen
    suspend fun getUsuario(): UsuarioConImagen {
        val idUsuario = idUserSP.getId()
        return usuarioDao.getUsuario(idUsuario)
    }

    //Funcion para obtener todos los usuarios con su correspondiente imagen
    fun getUsuariosConImagen(): List<UsuarioConImagen> {
        return usuarioDao.getUsuariosConImagen()
    }

    //Funcion para obtener el permiso del usuario
    suspend fun isAdmin() : Boolean {
        val permiso = usuarioDao.getPermiso(idUserSP.getId())
        return permiso == 1
    }

    //Funcion para buscar usuarios por nombre, usuario o correo
    suspend fun findUsuariosBy(valor: String): List<UsuarioConImagen> {
        return usuarioDao.findUsuariosBy(valor)
    }

    //Funcion para actualizar la contraseña
    suspend fun updatePassword(passwordByUser : String, newPassword: String, newPasswordD : String) : Response {
        val idUsuario = idUserSP.getId()
        val currPassword = usuarioDao.getPassword(idUsuario)

        if(passwordByUser.isEmpty() || newPassword.isEmpty() || newPasswordD.isEmpty()){
            return Response(false, "Los campos no pueden estar vacíos")
        }

        if(passwordByUser != currPassword){
            return Response(false, "La contraseña actual no coincide")
        }

        if(!coincidePassword(newPassword, newPasswordD)){
            return Response(false, "Las nuevas contraseñas no coinciden")
        }

        if(currPassword == newPassword){
            return Response(false,"La nueva contraseña no puede ser la misma que la anterior")
        }

        if(newPassword.length < 8){
            return Response(false, "La contraseña debe tener al menos 8 caracteres")
        }

        if(!isValidPassword(newPassword)){
            return Response(false, "La contraseña debe tener al menos una letra mayúscula, una letra minúscula y un número")
        }

        return if(usuarioDao.updatePassword(newPassword, idUsuario) > 0){
            Response(true, "Contraseña actualizada correctamente")
        } else {
            Response(false,"No se pudo cambiar la contraseña. Intente de nuevo")
        }
    }

    //Funcion para cambiar el permiso de un usuario
    suspend fun updatePermiso(idUsuario: Int) : Response {
        val idPermiso = usuarioDao.getPermiso(idUsuario)
        val permiso = if(idPermiso == 1) 2 else 1
        return if(usuarioDao.updatePermiso(permiso, idUsuario) > 0){
            Response(true,"Tipo de usuario modificado correctamente")
        } else {
            Response(false,"No fue posible modificar el tipo de usuario")
        }
    }

    //Funcion para actualizar el id de la imagen del usuario
    suspend fun updateIdImagen(idImagen: Int){
        if(usuarioDao.updateIdImagen(idImagen, idUserSP.getId()) > 0){
            Log.d("Id de imagen actualizada", "$idImagen")
        }else{
            Log.d("Error al actualizar id de imagen", "$idImagen")
        }
    }

    //Funcion para obtener el nombre del usuario
    suspend fun getNombre(): String{
        return usuarioDao.getNombre(idUserSP.getId())
    }

    //Funcion para eliminar un usuario
    suspend fun deleteUsuario(idUsuario: Int): Response{
        return if(usuarioDao.deleteUser(idUsuario) > 0){
            Response(true, "Usuario eliminado correctamente")
        }else{
            Response(false, "No se pudo eliminar el usuario")
        }
    }

    //Verifica si coinciden las contraseñas
    private fun coincidePassword(password: String, newPassword: String) = password == newPassword

    //Verifica si el correo ingresado es valido
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //Verifica si la contraseña ingresada usa el formato correcto
    private fun isValidPassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+\$".toRegex()
        return regex.matches(password)
    }

    // Verifica que el nombre no tenga numeros y caracteres especiales
    private fun validarNombre(nombre: String): Boolean {
        val regex = Regex("^[a-zA-ZÀ-ÿ\\s]*$")
        return regex.matches(nombre)
    }
}