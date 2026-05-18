package com.example.bodegazegarrahuerta.model

data class DetallePedido(
    val id: Int = 0,
    val pedidoId: Long = 0,
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Double
) {
    fun subtotal(): Double = cantidad * precioUnitario
}