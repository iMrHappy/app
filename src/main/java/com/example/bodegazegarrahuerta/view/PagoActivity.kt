package com.example.bodegazegarrahuerta.view

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.database.DatabaseHelper
import com.example.bodegazegarrahuerta.model.Pedido
import com.example.bodegazegarrahuerta.viewmodel.CarritoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PagoActivity : AppCompatActivity() {

    // CarritoViewModel con fábrica (igual que en CarritoActivity y ProductosActivity)
    private val carritoVM: CarritoViewModel by lazy {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CarritoViewModel(application as Application) as T
            }
        }
        ViewModelProvider(this, factory)[CarritoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        val toolbar = findViewById<Toolbar>(R.id.toolbarPago)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Datos de entrega"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val etNombre  = findViewById<EditText>(R.id.etNombreCliente)
        val etTel     = findViewById<EditText>(R.id.etTelefono)
        val etDir     = findViewById<EditText>(R.id.etDireccion)
        val btnPagar  = findViewById<Button>(R.id.btnConfirmarPago)

        btnPagar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val tel    = etTel.text.toString().trim()
            val dir    = etDir.text.toString().trim()

            if (nombre.isEmpty() || tel.isEmpty() || dir.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val items  = carritoVM.items.value ?: emptyList()
            if (items.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                finish()
                return@setOnClickListener
            }

            val total  = items.sumOf { it.subtotal() }
            val fecha  = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val db     = DatabaseHelper(this)

            val pedido = Pedido(
                clienteNombre = nombre,
                clienteTel    = tel,
                clienteDir    = dir,
                total         = total,
                fecha         = fecha
            )

            val detalles = items.map { Pair(it.producto.id, it.cantidad) }
            db.insertarPedido(pedido, detalles)

            // Obtener el código generado (último pedido insertado)
            val codigo = db.obtenerPedidos().firstOrNull()?.codigoSeguimiento ?: "N/A"

            // Mostrar dialog con código de seguimiento
            AlertDialog.Builder(this)
                .setTitle("Pedido confirmado")
                .setMessage(
                    "Tu pedido fue registrado.\n\n" +
                            "Código de seguimiento:\n\n$codigo\n\n" +
                            "Guárdalo para rastrear tu pedido."
                )
                .setCancelable(false)
                .setPositiveButton("Copiar código") { _, _ ->
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("codigo", codigo))
                    Toast.makeText(this, "Código copiado", Toast.LENGTH_SHORT).show()
                    carritoVM.limpiar()
                    finishAffinity()
                    startActivity(Intent(this, ProductosActivity::class.java))
                }
                .setNegativeButton("Aceptar") { _, _ ->
                    carritoVM.limpiar()
                    finishAffinity()
                    startActivity(Intent(this, ProductosActivity::class.java))
                }
                .show()
        }
    }
}