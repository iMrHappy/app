package com.example.bodegazegarrahuerta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.model.ItemCarrito

class CarritoAdapter(
    private var lista: List<ItemCarrito>,
    private val onQuitar: (ItemCarrito) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView   = view.findViewById(R.id.tvNombre)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidad)
        val tvSubtotal: TextView = view.findViewById(R.id.tvSubtotal)
        val btnQuitar: Button    = view.findViewById(R.id.btnQuitar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val item = lista[pos]
        h.tvNombre.text   = item.producto.nombre
        h.tvCantidad.text = "x${item.cantidad}"
        h.tvSubtotal.text = "S/ ${"%.2f".format(item.subtotal())}"
        h.btnQuitar.setOnClickListener { onQuitar(item) }
    }

    override fun getItemCount() = lista.size
    fun actualizar(nueva: List<ItemCarrito>) { lista = nueva; notifyDataSetChanged() }
}