package com.robertoguijon.login.ui.adapter.usuarios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robertoguijon.login.R
import com.robertoguijon.login.data.model.UsuarioConImagen
import com.robertoguijon.login.domain.listener.UsuariosListener

// Adaptador para la lista de usuarios
class UsuariosAdapter(
    private val users: List<UsuarioConImagen>,
    private val listener: UsuariosListener // Agrega el listener como parámetro
) : RecyclerView.Adapter<UsuariosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuariosViewHolder(layoutInflater.inflate(R.layout.card_usuarios, parent, false), listener)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        val item = users[position]

        holder.render(item)
    }
}