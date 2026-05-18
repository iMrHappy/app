package com.example.bodegazegarrahuerta.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.database.DatabaseHelper

class SeguimientoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seguimiento)

        val toolbar = findViewById<Toolbar>(R.id.toolbarSeguimiento)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Seguimiento de Pedido"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val etCodigo    = findViewById<EditText>(R.id.etCodigoPedido)
        val btnBuscar   = findViewById<Button>(R.id.btnBuscarPedido)
        val tvResultado = findViewById<TextView>(R.id.tvResultadoPedido)
        val db          = DatabaseHelper(this)

        btnBuscar.setOnClickListener {
            val codigo = etCodigo.text.toString().trim().uppercase()

            if (codigo.isEmpty()) {
                tvResultado.visibility = View.VISIBLE
                tvResultado.text = "Ingresa un codigo de seguimiento"
                return@setOnClickListener
            }

            val pedido = db.buscarPedidoPorCodigo(codigo)

            if (pedido == null) {
                tvResultado.text = "Pedido no encontrado.\nVerifica el codigo e intentalo de nuevo."
            } else {
                tvResultado.text = """
                    Cliente : ${pedido.clienteNombre}
                    Codigo  : ${pedido.codigoSeguimiento}
                    Estado  : ${pedido.status}
                    Fecha   : ${pedido.fecha}
                """.trimIndent()
            }
            tvResultado.visibility = View.VISIBLE
        }
    }
}