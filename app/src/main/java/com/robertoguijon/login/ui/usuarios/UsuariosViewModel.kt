package com.robertoguijon.login.ui.usuarios

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertoguijon.login.data.model.Response
import com.robertoguijon.login.domain.controller.UsersController
import com.robertoguijon.login.data.model.UsuarioConImagen
import com.robertoguijon.login.util.events.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UsuariosViewModel @Inject constructor(
    private val usersController: UsersController
) : ViewModel() {

    private var _users = MutableLiveData<List<UsuarioConImagen>>()
    val users : LiveData<List<UsuarioConImagen>> get() = _users
    private val _response = SingleLiveEvent<Response>()
    val response: LiveData<Response> get() = _response
    private val _isAdmin = MutableLiveData<Boolean>()
    val isAdmin: LiveData<Boolean> get() = _isAdmin

    //Verifica si el usuario tiene permisos de administrador
    fun onCreate(){
        viewModelScope.launch {
            _isAdmin.postValue(withContext(Dispatchers.IO){
                usersController.isAdmin()
            })
        }
    }

    //Obtiene los usuarios con su imagen
    fun getUsuarios(){
        viewModelScope.launch {
            _users.postValue(withContext(Dispatchers.IO) {
                usersController.getUsuariosConImagen()
            })
        }
    }

    //Cambia el permiso de un usuario
    fun cambiarPermiso(idUsuario: Int){
        viewModelScope.launch {
            _response.postValue(withContext(Dispatchers.IO){
                    usersController.updatePermiso(idUsuario)
                })
            getUsuarios()
        }
    }

    //Busca los usuarios por el nombre o el usuario
    fun buscarUsuarios(valor: String) {
        viewModelScope.launch {
            _users.postValue(withContext(Dispatchers.IO) {
                usersController.findUsuariosBy(valor)
            })
        }
    }

    //Elimina un usuario
    fun deleteUsuario(idUsuario: Int) {
        viewModelScope.launch {
            _response.postValue(withContext(Dispatchers.IO) {
                usersController.deleteUsuario(idUsuario)
            })
            getUsuarios()
        }
    }

}