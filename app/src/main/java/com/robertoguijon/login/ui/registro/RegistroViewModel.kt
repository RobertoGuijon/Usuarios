package com.robertoguijon.login.ui.registro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertoguijon.login.domain.controller.UsersController
import com.robertoguijon.login.data.model.Response
import com.robertoguijon.login.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistroViewModel @Inject constructor(
    private val usersController: UsersController
) : ViewModel() {

    private val _response = MutableLiveData<Response>()
    val response: MutableLiveData<Response> = _response

    //Funcion para registrar un usuario
    fun registrarUsuario(usuario: Usuario, passwordD: String) {
        viewModelScope.launch {
            _response.postValue(withContext(Dispatchers.IO) {
                usersController.registrarUsuario(usuario, passwordD)
            })
        }
    }

}