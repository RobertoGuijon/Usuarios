package com.robertoguijon.login.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertoguijon.login.domain.controller.UsersController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usersController : UsersController
) : ViewModel() {

    private var _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    //Funcion para obtener el nombre del usuario en sesion
    fun onCreate(){
        viewModelScope.launch {
            _nombre.postValue(usersController.getNombre())
        }
    }
}