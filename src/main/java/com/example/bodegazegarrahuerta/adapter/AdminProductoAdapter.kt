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

class AdminProductoAdapter(
    private var lista: List<Producto>,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit
) : RecyclerView.Adapter<AdminProductoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProducto  = view.findViewById<ImageView>(R.id.ivProductoAdmin)
        val tvNombre    = view.findViewById<TextView>(R.id.tvNombreAdmin)
        val tvCategoria = view.findViewById<TextView>(R.id.tvCategoriaAdmin)
        val tvPrecio    = view.findViewById<TextView>(R.id.tvPrecioAdmin)
        val btnEditar   = view.findViewById<Button>(R.id.btnEditar)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_admin, parent, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val p = lista[pos]
        h.tvNombre.text    = p.nombre
        h.tvCategoria.text = p.categoria
        h.tvPrecio.text    = "S/ ${"%.2f".format(p.precio)}  |  Stock: ${p.stock}"
        h.btnEditar.setOnClickListener   { onEditar(p) }
        h.btnEliminar.setOnClickListener { onEliminar(p) }

        // Imagen por categoría
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