package com.example.bodegazegarrahuerta.view

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.adapter.CarritoAdapter
import com.example.bodegazegarrahuerta.viewmodel.CarritoViewModel

class CarritoActivity : AppCompatActivity() {

    // ViewModel con fábrica porque ahora requiere Application en el constructor
    private val carritoVM: CarritoViewModel by lazy {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CarritoViewModel(application as Application) as T
            }
        }
        ViewModelProvider(this, factory)[CarritoViewModel::class.java]
    }

    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val tvTotal = findViewById<TextView>(R.id.tvTotal)

        // Inicializar adapter con lista vacía y acción de eliminar
        adapter = CarritoAdapter(emptyList()) { item ->
            carritoVM.eliminar(item)
        }

        val rv = findViewById<RecyclerView>(R.id.rvCarrito)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Observar cambios en el carrito
        carritoVM.items.observe(this) { items ->
            adapter.actualizar(items)
            val total = items.sumOf { it.subtotal() }
            tvTotal.text = "Total: S/ ${"%.2f".format(total)}"
        }

        // Botón pagar
        findViewById<Button>(R.id.btnPagar).setOnClickListener {
            if (carritoVM.items.value.isNullOrEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(Intent(this, PagoActivity::class.java))
        }

        // Botón volver
        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
}