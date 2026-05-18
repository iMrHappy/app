package com.example.bodegazegarrahuerta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.model.Producto

class ProductoAdminAdapter(
    private var lista: List<Producto>,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdminAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView    = view.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvPrecio: TextView    = view.findViewById(R.id.tvPrecio)
        val btnEditar: Button     = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button   = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_admin, parent, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val p = lista[pos]
        h.tvNombre.text    = p.nombre
        h.tvCategoria.text = p.categoria
        h.tvPrecio.text    = "S/ ${"%.2f".format(p.precio)}"
        h.btnEditar.setOnClickListener   { onEditar(p) }
        h.btnEliminar.setOnClickListener { onEliminar(p) }
    }

    override fun getItemCount() = lista.size
    fun actualizar(nueva: List<Producto>) { lista = nueva; notifyDataSetChanged() }
}