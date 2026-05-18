package com.example.bodegazegarrahuerta.view

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.adapter.ProductoAdapter
import com.example.bodegazegarrahuerta.model.Producto
import com.example.bodegazegarrahuerta.repository.ProductoRepository
import com.example.bodegazegarrahuerta.viewmodel.CarritoViewModel
import com.example.bodegazegarrahuerta.viewmodel.ProductoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductosActivity : AppCompatActivity() {

    // ProductoViewModel con su repositorio
    private val productoVM: ProductoViewModel by lazy {
        // Necesitas el repositorio; si usas Retrofit, deberías obtenerlo de BodegaApp o inyectarlo.
        // Suponiendo que tienes un método en BodegaApp para obtener ProductoRepository:
        val repo = (application as BodegaApp).productoRepository
        ProductoViewModel(repo)
    }

    // CarritoViewModel con fábrica (como antes)
    private val carritoVM: CarritoViewModel by lazy {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CarritoViewModel(application as Application) as T
            }
        }
        ViewModelProvider(this, factory)[CarritoViewModel::class.java]
    }

    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        val rvProductos = findViewById<RecyclerView>(R.id.rvProductos)
        rvProductos.layoutManager = GridLayoutManager(this, 2)

        adapter = ProductoAdapter(emptyList()) { producto ->
            agregarAlCarrito(producto)
        }
        rvProductos.adapter = adapter

        // Observar lista de productos
        productoVM.productos.observe(this) { productos ->
            adapter.actualizar(productos)
        }

        // Observar mensajes (errores o éxitos)
        productoVM.mensaje.observe(this) { mensaje ->
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        }

        // Cargar productos al iniciar
        productoVM.cargar()

        // Configurar BottomNavigationView
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Mostrar ofertas (por implementar)
                    Toast.makeText(this, "Ofertas", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_productos -> {
                    // Ya estamos en productos
                    true
                }
                R.id.nav_seguimiento -> {
                    startActivity(Intent(this, SeguimientoActivity::class.java))
                    true
                }
                R.id.nav_acceder -> {
                    startActivity(Intent(this, LoginAdminActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun agregarAlCarrito(producto: Producto) {
        carritoVM.agregar(producto)
        Toast.makeText(this, "Agregado: ${producto.nombre}", Toast.LENGTH_SHORT).show()
    }
}