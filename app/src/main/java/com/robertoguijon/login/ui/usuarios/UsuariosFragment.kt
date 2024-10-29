package com.robertoguijon.login.ui.usuarios

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.robertoguijon.login.LoginApp.Companion.idUserSP
import com.robertoguijon.login.R
import com.robertoguijon.login.databinding.FragmentUsuariosBinding
import com.robertoguijon.login.domain.listener.UsuariosListener
import com.robertoguijon.login.ui.adapter.usuarios.UsuariosAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsuariosFragment : Fragment(), UsuariosListener{

    private var _binding: FragmentUsuariosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val usuariosViewModel : UsuariosViewModel by viewModels()
    private lateinit var navController: NavController //Este nos va a permitir redireccionar a la pagina de error
    private lateinit var deleteConfirmationDialog: AlertDialog //Es el dialogo de confirmacion para eliminar un usuario


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsuariosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        usuariosViewModel.onCreate()

        //Muestra la lista de usuarios
        usuariosViewModel.users.observe(viewLifecycleOwner){
            binding.rvUsuarios.layoutManager = LinearLayoutManager(binding.rvUsuarios.context, LinearLayoutManager.VERTICAL, false)
            binding.rvUsuarios.adapter = UsuariosAdapter(it, this)
        }

        //Muestra la respuesta cuando se elimina o cambia el permiso de un usuario
        usuariosViewModel.response.observe(viewLifecycleOwner) { response ->
            response?.let {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        //Busca los usuarios cuando se escribe en el buscador
        binding.svUsuarios.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(text: String?): Boolean {
                usuariosViewModel.buscarUsuarios(text ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                usuariosViewModel.buscarUsuarios(newText ?: "")
                return true
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Verifica si el usuario tiene permisos de administrador
        usuariosViewModel.isAdmin.observe(viewLifecycleOwner){
            if(!it){
                //Si no tiene permisos de administrador se redirige a la pantalla de error
                navController = findNavController()
                navController.navigate(R.id.action_navigation_usuarios_to_navigation_error)
            }else{
                usuariosViewModel.getUsuarios()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Se llama cuando se cambia el permiso de un usuario
    override fun onSwitchPermisoChanged(idUsuario: Int) {
        usuariosViewModel.cambiarPermiso(idUsuario)
    }

    //Se llama cuando se elimina un usuario
    override fun onDeleteUser(idUsuario: Int) {
        if(idUsuario != 1 && idUsuario != idUserSP.getId()){
            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Eliminar usuario")
                .setMessage("¿Estás seguro de que quieres eliminar este usuario?")
                .setPositiveButton("Sí") { _, _ ->
                    usuariosViewModel.deleteUsuario(idUsuario)
                }
                .setNegativeButton("No", null)

            deleteConfirmationDialog = builder.create()
            deleteConfirmationDialog.show()
        }
    }

}