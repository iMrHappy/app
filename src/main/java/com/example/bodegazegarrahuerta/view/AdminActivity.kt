package com.example.bodegazegarrahuerta.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.adapter.AdminProductoAdapter
import com.example.bodegazegarrahuerta.adapter.PedidoAdapter
import com.example.bodegazegarrahuerta.database.DatabaseHelper
import com.example.bodegazegarrahuerta.repository.ProductoRepository
import com.example.bodegazegarrahuerta.viewmodel.ProductoViewModel
import com.example.bodegazegarrahuerta.viewmodel.ProductoViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    private val productoVM: ProductoViewModel by lazy {
        ViewModelProvider(
            this,
            ProductoViewModelFactory(ProductoRepository())
        )[ProductoViewModel::class.java]
    }

    private lateinit var db: DatabaseHelper
    private lateinit var adminAdapter: AdminProductoAdapter
    private lateinit var pedidoAdapter: PedidoAdapter

    private lateinit var panelProductos: LinearLayout
    private lateinit var panelPedidos: LinearLayout
    private lateinit var panelStats: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        db = DatabaseHelper(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbarAdmin)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Panel Administrador"

        panelProductos = findViewById(R.id.panelProductos)
        panelPedidos   = findViewById(R.id.panelPedidos)
        panelStats     = findViewById(R.id.panelStats)

        configurarPanelProductos()
        configurarPanelPedidos()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavAdmin)
        bottomNav.selectedItemId = R.id.admin_nav_productos

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_nav_productos -> {
                    mostrarPanel(panelProductos)
                    supportActionBar?.title = "Gestionar Productos"
                    productoVM.cargar()
                    true
                }
                R.id.admin_nav_pedidos -> {
                    mostrarPanel(panelPedidos)
                    supportActionBar?.title = "Pedidos"
                    cargarPedidos()
                    true
                }
                R.id.admin_nav_stats -> {
                    mostrarPanel(panelStats)
                    supportActionBar?.title = "Estadisticas"
                    cargarEstadisticas()
                    true
                }
                R.id.admin_nav_salir -> {
                    startActivity(Intent(this, ProductosActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        productoVM.cargar()
    }

    private fun configurarPanelProductos() {
        adminAdapter = AdminProductoAdapter(
            emptyList(),
            onEditar = { producto ->
                val intent = Intent(this, FormProductoActivity::class.java)
                intent.putExtra("producto_id", producto.id)
                startActivity(intent)
            },
            onEliminar = { producto ->
                productoVM.eliminar(producto.id)
                Toast.makeText(this, "${producto.nombre} eliminado", Toast.LENGTH_SHORT).show()
            }
        )

        val rvAdmin = findViewById<RecyclerView>(R.id.rvAdmin)
        rvAdmin.layoutManager = LinearLayoutManager(this)
        rvAdmin.adapter = adminAdapter

        productoVM.productos.observe(this) { lista ->
            adminAdapter.actualizar(lista)
        }

        findViewById<android.widget.Button>(R.id.btnNuevoProducto).setOnClickListener {
            startActivity(Intent(this, FormProductoActivity::class.java))
        }
    }

    private fun configurarPanelPedidos() {
        pedidoAdapter = PedidoAdapter(emptyList()) { pedido, nuevoStatus ->
            db.actualizarStatusPedido(pedido.id, nuevoStatus)
            cargarPedidos()
            Toast.makeText(this, "Status actualizado: $nuevoStatus", Toast.LENGTH_SHORT).show()
        }

        val rvPedidos = findViewById<RecyclerView>(R.id.rvPedidos)
        rvPedidos.layoutManager = LinearLayoutManager(this)
        rvPedidos.adapter = pedidoAdapter
    }

    private fun cargarPedidos() {
        val pedidos = db.obtenerPedidos()
        pedidoAdapter.actualizar(pedidos)
    }

    private fun cargarEstadisticas() {
        val pedidos = db.obtenerPedidos()
        val totalPedidos = pedidos.size
        val totalVendido = pedidos.sumOf { it.total }
        val porStatus = pedidos.groupBy { it.status }
            .map { (status, lista) -> "$status: ${lista.size}" }
            .joinToString("\n")

        findViewById<TextView>(R.id.tvTotalPedidos).text     = "Total pedidos: $totalPedidos"
        findViewById<TextView>(R.id.tvTotalVendido).text     = "Total vendido: S/ ${"%.2f".format(totalVendido)}"
        findViewById<TextView>(R.id.tvPedidosPorStatus).text = "Por status:\n$porStatus"
        findViewById<TextView>(R.id.tvProductoTop).text      = "Ver detalle de productos en desarrollo"
    }

    private fun mostrarPanel(panel: LinearLayout) {
        panelProductos.visibility = View.GONE
        panelPedidos.visibility   = View.GONE
        panelStats.visibility     = View.GONE
        panel.visibility          = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        productoVM.cargar()
    }
}