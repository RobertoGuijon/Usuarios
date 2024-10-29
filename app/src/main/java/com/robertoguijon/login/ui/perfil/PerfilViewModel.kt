package com.robertoguijon.login.ui.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertoguijon.login.data.model.Imagen
import com.robertoguijon.login.data.model.Response
import com.robertoguijon.login.data.model.UsuarioConImagen
import com.robertoguijon.login.domain.controller.ImagenesController
import com.robertoguijon.login.domain.controller.UsersController
import com.robertoguijon.login.util.events.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val usersController: UsersController,
    private val imagenesController: ImagenesController
): ViewModel(){

    private var _user = MutableLiveData<UsuarioConImagen>()
    val user: LiveData<UsuarioConImagen> = _user
    private val _response = SingleLiveEvent<Response>()
    val response: LiveData<Response> get() = _response
    private val _responsePassword = SingleLiveEvent<Response>()
    val responsePassword: LiveData<Response> get() = _responsePassword
    private val _responseAddImage = SingleLiveEvent<Pair<Response, Int>>()
    val responseAddImage: LiveData<Pair<Response, Int>> get() = _responseAddImage

    //Funcion para obtener el usuario en sesion
    fun onCreate(){
        viewModelScope.launch {
            _user.postValue(withContext(Dispatchers.IO) {
                usersController.getUsuario()
            })
        }
    }

    //Funcion para actualizar la contraseña
    fun updatePassword(contraseniaActual: String, nuevaContrasenia: String, confContrasenia: String){
        viewModelScope.launch {
            _responsePassword.postValue(withContext(Dispatchers.IO){
                usersController.updatePassword(contraseniaActual, nuevaContrasenia, confContrasenia)
            })
        }
    }

    //Funcion para actualizar la imagen
    fun updateImage(imagen: Imagen){
        viewModelScope.launch {
            _response.postValue(withContext(Dispatchers.IO){
                imagenesController.updateImagen(imagen)
            })
            onCreate()
        }
    }

    //Funcion para agregar la imagen
    fun addImagen(imagen: Imagen){
        viewModelScope.launch {
            _responseAddImage.postValue(withContext(Dispatchers.IO){
                imagenesController.addImagen(imagen)
            })
        }
    }

    //Funcion para actualizar el id de la imagen en el usuario
    fun updateIdImagen(idImagen: Int){
        viewModelScope.launch(Dispatchers.IO) {
            usersController.updateIdImagen(idImagen)
            onCreate()
        }
    }

}