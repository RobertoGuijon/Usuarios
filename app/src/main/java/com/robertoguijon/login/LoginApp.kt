package com.robertoguijon.login

import android.app.Application
import android.content.Intent
import com.robertoguijon.login.ui.login.LoginActivity
import com.robertoguijon.login.util.sharedPreferences.UserSharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LoginApp : Application(){

    companion object{
        lateinit var idUserSP : UserSharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        //Inicializa el sharedPreferences
        idUserSP = UserSharedPreferences(applicationContext)
    }
}