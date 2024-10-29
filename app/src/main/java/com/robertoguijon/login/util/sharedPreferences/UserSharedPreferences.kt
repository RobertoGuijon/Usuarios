package com.robertoguijon.login.util.sharedPreferences

import android.content.Context

//Clase que nos permite guardar el id del usuario en sesion, cerrar sesiones
class UserSharedPreferences(context : Context) {

    private val SHARED_NAME = "IdUserDB"
    private val SHARED_ID = "IdUser"

    private val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveId(idUser : Int){
        storage.edit().putInt(SHARED_ID, idUser).apply()
    }

    fun getId() : Int{
        return storage.getInt(SHARED_ID, -1)
    }

    fun logOut(){
        storage.edit().putInt(SHARED_ID, -1).apply()
    }
}