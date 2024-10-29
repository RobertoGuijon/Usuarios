package com.robertoguijon.login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertoguijon.login.domain.controller.UsersController
import com.robertoguijon.login.data.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usersController: UsersController
) : ViewModel() {

    private val _response = MutableLiveData<Response>()
    val response : LiveData<Response> get()  = _response

    //Funcion para iniciar sesion
    fun login(usuario: String, password: String) {
        viewModelScope.launch {
            _response.postValue(usersController.login(usuario, password))
        }
    }
}