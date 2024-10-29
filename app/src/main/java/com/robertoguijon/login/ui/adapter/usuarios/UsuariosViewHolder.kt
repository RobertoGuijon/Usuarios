package com.robertoguijon.login.ui.adapter.usuarios

import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.robertoguijon.login.LoginApp.Companion.idUserSP
import com.robertoguijon.login.data.model.UsuarioConImagen
import com.robertoguijon.login.databinding.CardUsuariosBinding
import com.robertoguijon.login.domain.listener.UsuariosListener

// ViewHolder que representa un elemento individual dentro del RecyclerView
class UsuariosViewHolder(
    view: View,
    private val listener: UsuariosListener
) : RecyclerView.ViewHolder(view) {

    private val binding = CardUsuariosBinding.bind(view)

    // Renderiza los datos del usuario en la vista
    fun render(usuario: UsuarioConImagen) {

        binding.tvNombreCard.text = usuario.nombre
        binding.tvUsuarioCard.text = usuario.usuario
        binding.tvCorreoCard.text = usuario.correo
        val bitmap = BitmapFactory.decodeByteArray(usuario.imagen, 0, usuario.imagen.size) //Convierte la imagen en un bitmap
        binding.ivUsuarioCard.setImageBitmap(bitmap) //Muestra la imagen del usuario
        var success = false
        //Si el usuario es el 1 o es el usuario en sesion no se puede modificar su permiso ni eliminarlo
        if(usuario.idUsuario == 1 || usuario.idUsuario == idUserSP.getId()){
            disable()
        }

        //Define el estado del switch de acuerdo al permiso del usuario
        switchChecked(usuario.idPermiso == 1)

        success = true

        //Al cambiar el estado del switch se llama al listener para cambiar el permiso
        binding.switchPermisoCard.setOnCheckedChangeListener { _, _ ->
            if(success){
                listener.onSwitchPermisoChanged(usuario.idUsuario)
            }
        }

        //Al mantener presionado el usuario se llama al listener para eliminar el usuario
        binding.cardviewUsuarios.setOnLongClickListener {
            listener.onDeleteUser(usuario.idUsuario)
            true
        }
    }

    //Deshabilita el switch y el cardview
    private fun disable(){
        binding.switchPermisoCard.isEnabled = false
        binding.cardviewUsuarios.isClickable = false
        binding.cardviewUsuarios.isFocusable = false
    }

    //Cambia el estado del switch
    private fun switchChecked(checked: Boolean){
        binding.switchPermisoCard.isChecked = checked
    }
}