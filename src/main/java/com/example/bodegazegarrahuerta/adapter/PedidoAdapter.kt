package com.example.bodegazegarrahuerta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodegazegarrahuerta.R
import com.example.bodegazegarrahuerta.model.Pedido

class PedidoAdapter(
    private var lista: List<Pedido>,
    private val onActualizar: (Pedido, String) -> Unit
) : RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    private val statusOpciones = listOf(
        "Preparando pedido",
        "En camino",
        "Entregado",
        "Cancelado"
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCodigo         = view.findViewById<TextView>(R.id.tvCodigo)
        val tvCliente        = view.findViewById<TextView>(R.id.tvClientePedido)
        val tvFecha          = view.findViewById<TextView>(R.id.tvFechaPedido)
        val tvTotal          = view.findViewById<TextView>(R.id.tvTotalPedido)
        val spinnerStatus    = view.findViewById<Spinner>(R.id.spinnerStatus)
        val btnActualizar    = view.findViewById<Button>(R.id.btnActualizarStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val pedido = lista[pos]
        h.tvCodigo.text  = pedido.codigoSeguimiento
        h.tvCliente.text = "Cliente: ${pedido.clienteNombre}  |  Tel: ${pedido.clienteTel}"
        h.tvFecha.text   = pedido.fecha
        h.tvTotal.text   = "S/ ${"%.2f".format(pedido.total)}"

        val adapter = ArrayAdapter(
            h.itemView.context,
            android.R.layout.simple_spinner_item,
            statusOpciones
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        h.spinnerStatus.adapter = adapter
        val idx = statusOpciones.indexOf(pedido.status).takeIf { it >= 0 } ?: 0
        h.spinnerStatus.setSelection(idx)

        h.btnActualizar.setOnClickListener {
            val nuevoStatus = h.spinnerStatus.selectedItem.toString()
            onActualizar(pedido, nuevoStatus)
        }
    }

    override fun getItemCount() = lista.size

    fun actualizar(nueva: List<Pedido>) {
        lista = nueva
        notifyDataSetChanged()
    }
}