package com.robertoguijon.login.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.robertoguijon.login.databinding.ActivityLoginBinding
import com.robertoguijon.login.ui.MainActivity
import com.robertoguijon.login.ui.registro.RegistroActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Nos dirige a la pantalla de registro
        binding.tvRegistrarseLogin.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }

        //Al presionar el boton de iniciar sesion se llama al viewmodel para iniciar sesion
        binding.fabNextLogin.setOnClickListener {
            val email = binding.tilCorreoLogin.editText?.text.toString()
            val password = binding.tilContraseniaLogin.editText?.text.toString()
            loginViewModel.login(email, password)
        }

        //Se llama a la funcion para iniciar sesion
        loginViewModel.response.observe(this) {
            //Si el inicio de sesion es exitoso se dirige a la pantalla principal
            if(it.success){
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}