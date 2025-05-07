package com.example.project_kotlin.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.project_kotlin.R
import com.example.project_kotlin.dao.EmpleadoDao
import com.example.project_kotlin.dao.UsuarioDao
import com.example.project_kotlin.db.ComandaDatabase
import com.example.project_kotlin.utils.VariablesGlobales
import com.example.project_kotlin.utils.appConfig
import com.example.project_kotlin.vistas.inicio.IndexComandasActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var btnIngresar: Button
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var empleadoDao: EmpleadoDao
    private lateinit var edtUsuario: EditText
    private lateinit var edtContraseña: EditText
    private lateinit var imgShow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //esto hace que la barra de notificaciones sea negro
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        edtUsuario = findViewById(R.id.edtUsuario)
        edtContraseña = findViewById(R.id.edtPassword)
        btnIngresar = findViewById(R.id.btnIngresar)
        imgShow=findViewById(R.id.imgShow)
        usuarioDao = ComandaDatabase.obtenerBaseDatos(appConfig.CONTEXT).usuarioDao()
        empleadoDao = ComandaDatabase.obtenerBaseDatos(appConfig.CONTEXT).empleadoDao()
        btnIngresar.setOnClickListener({ vincular() })
        imgShow.setOnClickListener {
            val selection = edtContraseña.selectionStart // 🔸 Guarda la posición actual del cursor

            if (edtContraseña.transformationMethod == PasswordTransformationMethod.getInstance()) {
                // Mostrar contraseña
                imgShow.setImageResource(R.drawable.icon_close_eye)
                edtContraseña.transformationMethod = HideReturnsTransformationMethod.getInstance()

            } else {
                // Ocultar contraseña
                imgShow.setImageResource(R.drawable.icon_open_eye)
                edtContraseña.transformationMethod = PasswordTransformationMethod.getInstance()

            }
            edtContraseña.setSelection(selection) // 🔸 Restaura la posición del cursor
        }

    }

    fun vincular() {
        val email = edtUsuario.text.toString()
        val password = edtContraseña.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val verificarusuario = usuarioDao.verificarUsuarioYContrasenia(email, password)

                launch(Dispatchers.Main) {
                    if (verificarusuario != null) {
                        try {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val id = verificarusuario.id
                                if (id != null) {
                                    VariablesGlobales.empleado = empleadoDao.obtenerPorId(id)
                                }
                            }
                            mostrarToast("Ingresando al sistema")
                            val intent =
                                Intent(this@MainActivity, IndexComandasActivity::class.java)
                            startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            mostrarToast("Error al cargar datos del empleado: ${e.message}")
                        }
                    } else {
                        mostrarToast("Verifique su usuario o contraseña")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    mostrarToast("Error al verificar usuario: ${e.message}")
                }
            }
        }
}
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun mostrarToast(mensaje: String) {
        runOnUiThread {
            Toast.makeText(appConfig.CONTEXT, mensaje, Toast.LENGTH_SHORT).show()
        }
    }
}



