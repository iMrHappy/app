package com.example.bodegazegarrahuerta.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.database.DatabaseHelper

class LoginAdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        val toolbar = findViewById<Toolbar>(R.id.toolbarLoginAdmin)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Acceso Administrador"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val etUsuario  = findViewById<EditText>(R.id.etUsuarioAdmin)
        val etPassword = findViewById<EditText>(R.id.etPasswordAdmin)
        val btnIngresar = findViewById<Button>(R.id.btnIngresarAdmin)

        btnIngresar.setOnClickListener {
            val usuario  = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = DatabaseHelper(this)
            if (db.validarAdmin(usuario, password)) {
                Toast.makeText(this, "Bienvenido, $usuario", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}