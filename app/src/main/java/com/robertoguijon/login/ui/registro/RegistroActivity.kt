package com.robertoguijon.login.ui.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.robertoguijon.login.databinding.ActivityRegistroBinding
import com.robertoguijon.login.data.model.Usuario
import com.robertoguijon.login.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val registroViewModel : RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Al presionar el boton de registrar se llama al viewmodel para registrar el usuario
        binding.fabNextRegistro.setOnClickListener {
            val usuario = Usuario(
                idUsuario = 0,
                nombre = binding.tilNombreCompleto.editText?.text.toString(),
                usuario = binding.tilUsuario.editText?.text.toString(),
                correo = binding.tilCorreo.editText?.text.toString(),
                password = binding.tilContrasenia.editText?.text.toString(),
                idPermiso = 2,
                idImagen = 1
            )
            val passwordD = binding.tilConfirmarContrasenia.editText?.text.toString()
            registroViewModel.registrarUsuario(usuario, passwordD)
        }

        //Muestra la respuesta del registro y redirige al inicio de sesion
        registroViewModel.response.observe(this) {
            if (it.success) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }

        //Nos dirige a la pantalla de inicio de sesion
        binding.tvIniciarSesionRegistro.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}