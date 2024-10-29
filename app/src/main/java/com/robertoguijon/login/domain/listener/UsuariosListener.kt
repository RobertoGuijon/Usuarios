package com.robertoguijon.login.domain.listener

//Interfaz que conecta el ViewHolder con el Fragment Usuarios
interface UsuariosListener {

    //Funcion para identificar cuando se cambia el permiso
    fun onSwitchPermisoChanged(idUsuario: Int)

    //funcion para identificar cuando se quiere eliminar un usuario
    fun onDeleteUser(idUsuario: Int)
}