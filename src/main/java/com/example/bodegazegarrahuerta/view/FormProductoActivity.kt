package com.example.bodegazegarrahuerta.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.model.Producto
import com.example.bodegazegarrahuerta.repository.ProductoRepository
import com.example.bodegazegarrahuerta.viewmodel.ProductoViewModel
import com.example.bodegazegarrahuerta.viewmodel.ProductoViewModelFactory

class FormProductoActivity : AppCompatActivity() {

    private val productoVM: ProductoViewModel by lazy {
        ViewModelProvider(
            this,
            ProductoViewModelFactory(ProductoRepository())
        )[ProductoViewModel::class.java]
    }

    private var productoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_producto)

        val toolbar     = findViewById<Toolbar>(R.id.toolbarForm)
        val etNombre    = findViewById<EditText>(R.id.etNombreForm)
        val etCategoria = findViewById<EditText>(R.id.etCategoriaForm)
        val etPrecio    = findViewById<EditText>(R.id.etPrecioForm)
        val etStock     = findViewById<EditText>(R.id.etStockForm)
        val btnGuardar  = findViewById<Button>(R.id.btnGuardarProducto)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        productoId = intent.getIntExtra("producto_id", -1)

        if (productoId != -1) {
            supportActionBar?.title = "Editar Producto"
            // Cargar productos y buscar el que coincide con el ID
            productoVM.cargar()
            productoVM.productos.observe(this) { lista ->
                val producto = lista.find { it.id == productoId }
                producto?.let {
                    etNombre.setText(it.nombre)
                    etCategoria.setText(it.categoria)
                    etPrecio.setText(it.precio.toString())
                    etStock.setText(it.stock.toString())
                }
            }
        } else {
            supportActionBar?.title = "Nuevo Producto"
        }

        // Observar mensajes de resultado
        productoVM.mensaje.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        btnGuardar.setOnClickListener {
            val nombre    = etNombre.text.toString().trim()
            val categoria = etCategoria.text.toString().trim()
            val precio    = etPrecio.text.toString().toDoubleOrNull()
            val stock     = etStock.text.toString().toIntOrNull()

            if (nombre.isEmpty() || categoria.isEmpty() || precio == null || stock == null) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val producto = Producto(
                id        = productoId,
                nombre    = nombre,
                categoria = categoria,
                precio    = precio,
                stock     = stock,
                imagen    = ""
            )

            if (productoId == -1) {
                productoVM.insertar(producto)   // ← guarda en MySQL via PHP
            } else {
                productoVM.actualizar(producto) // ← actualiza en MySQL via PHP
            }

            finish()
        }
    }
}