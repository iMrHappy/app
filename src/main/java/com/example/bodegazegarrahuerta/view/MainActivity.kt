package com.example.bodegazegarrahuerta.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.database.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DatabaseHelper(this)

        val etUsuario  = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        findViewById<Button>(R.id.btnAdmin).setOnClickListener {
            val u = etUsuario.text.toString().trim()
            val p = etPassword.text.toString().trim()
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (db.validarAdmin(u, p))
                startActivity(Intent(this, AdminActivity::class.java))
            else
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnCliente).setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }
    }
}