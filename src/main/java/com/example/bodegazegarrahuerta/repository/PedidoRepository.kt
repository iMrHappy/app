package com.example.bodegazegarrahuerta.repository

import com.example.bodegazegarrahuerta.database.DatabaseHelper
import com.example.bodegazegarrahuerta.model.Pedido

class PedidoRepository(private val db: DatabaseHelper) {
    fun insertarPedido(p: Pedido, detalles: List<Pair<Int, Int>>): Long =
        db.insertarPedido(p, detalles)
}