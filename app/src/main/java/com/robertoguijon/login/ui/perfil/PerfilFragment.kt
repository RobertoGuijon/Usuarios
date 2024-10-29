package com.robertoguijon.login.ui.perfil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.robertoguijon.login.R
import com.robertoguijon.login.data.model.Imagen
import com.robertoguijon.login.databinding.FragmentPerfilBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayInputStream

@AndroidEntryPoint
class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val perfilViewModel : PerfilViewModel by viewModels()
    private var idImagen: Int = 0
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest> //Nos permite seleccionar una imagen de la galeria

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Nos permite seleccionar una imagen de la galeria
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uploadImage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        perfilViewModel.onCreate()

        renderImage()

        //Muestra los datos del usuario en sesion
        perfilViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tiNombrePerfil.text = Editable.Factory.getInstance().newEditable(user.nombre)
            binding.tiUsuarioPerfil.text = Editable.Factory.getInstance().newEditable(user.usuario)
            binding.tiCorreoPerfil.text = Editable.Factory.getInstance().newEditable(user.correo)
            binding.tvPermisosPerfil.text = if (user.idPermiso == 1) "Administrador" else "Usuario"

            // Llamar a la función para corregir la orientación de la imagen
            val orientedBitmap = getCorrectlyOrientedImage(user.imagen)

            // Establecer el Bitmap corregido en el ImageView
            if (orientedBitmap != null) {
                binding.ivImagenPerfil.setImageBitmap(orientedBitmap)
            }

            idImagen = user.idImagen
        }

        //Nos permite cambiar la imagen de perfil
        binding.ivEditarImagen.setOnClickListener {
            openImagePicker()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Al cambiar el estado del switch de contraseña se muestra u oculta las cajas de texto
        binding.swContraseniaPerfil.setOnCheckedChangeListener { _, isChecked ->
            binding.llyContraseniaPerfil.visibility = if (isChecked) View.VISIBLE else View.GONE
            if (!isChecked) {
                cleanFields()
            }
        }

        //Permite actualizar contraseña
        binding.btnActualizarPerfil.setOnClickListener {
            val contraseniaActual = binding.tilContraseniaActualPerfil.editText?.text.toString()
            val nuevaContrasenia = binding.tilNuevaContraseniaPerfil.editText?.text.toString()
            val confContrasenia = binding.tilConfContraseniaPerfil.editText?.text.toString()
            perfilViewModel.updatePassword(contraseniaActual, nuevaContrasenia, confContrasenia)
        }

        //Se muestra un mensaje de respuesta cuando se actualiza la imagen
        perfilViewModel.response.observe(viewLifecycleOwner) { response ->
            response?.let {
                showToast(it.message)
            }
        }

        //Se muestra un mensaje de respuesta cuando se agrega la imagem
        perfilViewModel.responseAddImage.observe(viewLifecycleOwner) { response ->
            response?.let {
                showToast(it.first.message)
                perfilViewModel.updateIdImagen(it.second)
                perfilViewModel.onCreate()
            }
        }

        //Se muestra un mensaje de respuesta cuando se cambia la contraseña
        perfilViewModel.responsePassword.observe(viewLifecycleOwner) { response ->
            response?.let {
                showToast(it.message)
                if(it.success){
                    binding.swContraseniaPerfil.isChecked = false
                }
            }
        }
    }

    //Nos permite seleccionar una imagen de la galeria
    private fun openImagePicker() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    //Actualiza o agrega la imagen
    private fun uploadImage(imageUri: Uri) {
        try {
            requireContext().contentResolver.openInputStream(imageUri)?.use { inputStream ->
                val imageBytes = inputStream.readBytes()
                if(idImagen == 1){
                    val imagen = Imagen(0, imageBytes)
                    perfilViewModel.addImagen(imagen)
                    return
                }
                val imagen = Imagen(idImagen, imageBytes)
                perfilViewModel.updateImage(imagen)
            }
        } catch (e: Exception) {
            Log.e("UploadImage", "Error al subir la imagen", e)
        }
    }

    //Funcion que nos permite mostrar un Toast con un texto personalizado
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun renderImage(){
        binding.ivEditarImagen.setImageResource(R.drawable.editar)
    }

    fun getCorrectlyOrientedImage(imageBytes: ByteArray): Bitmap? {
        // Crear un InputStream para leer la imagen
        val inputStream = ByteArrayInputStream(imageBytes)

        // Leer la imagen en un Bitmap
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Crear un nuevo InputStream para leer los datos EXIF
        val exifInputStream = ByteArrayInputStream(imageBytes)
        val exif = ExifInterface(exifInputStream)

        // Obtener la orientación de la imagen
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        // Crear un matrix para la transformación
        val matrix = Matrix()

        // Ajustar la matriz según la orientación
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        // Crear un nuevo Bitmap con la orientación correcta
        return Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    //Limpia las cajas de texto de la contraseña
    private fun cleanFields() {
        binding.tilContraseniaActualPerfil.editText?.text?.clear()
        binding.tilNuevaContraseniaPerfil.editText?.text?.clear()
        binding.tilConfContraseniaPerfil.editText?.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}