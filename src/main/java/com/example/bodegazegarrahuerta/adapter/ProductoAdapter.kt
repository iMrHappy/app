package com.example.bodegazegarrahuerta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.model.Producto

class ProductoAdapter(
    private var lista: List<Producto>,
    private val onAgregar: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProducto: ImageView = view.findViewById(R.id.ivProducto)
        val tvNombre: TextView    = view.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvPrecio: TextView    = view.findViewById(R.id.tvPrecio)
        val btnAgregar: Button    = view.findViewById(R.id.btnAgregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val p = lista[pos]
        h.tvNombre.text    = p.nombre
        h.tvCategoria.text = p.categoria
        h.tvPrecio.text    = "S/ ${"%.2f".format(p.precio)}"
        h.btnAgregar.setOnClickListener { onAgregar(p) }

        // Imagen por categoría desde drawable
        val imagenRes = when (p.categoria.lowercase().trim()) {
            "abarrotes"  -> R.drawable.img_abarrotes
            "limpieza"   -> R.drawable.img_limpieza
            "plasticos"  -> R.drawable.img_plasticos
            "ferreteria" -> R.drawable.img_ferreteria
            else         -> R.drawable.img_abarrotes
        }
        h.ivProducto.setImageResource(imagenRes)
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Producto>) {
        lista = nueva
        notifyDataSetChanged()
    }
}