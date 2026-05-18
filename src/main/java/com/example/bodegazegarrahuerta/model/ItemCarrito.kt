package com.example.bodegazegarrahuerta.model

data class ItemCarrito(
    val producto: Producto,
    var cantidad: Int = 1
) {
    fun subtotal(): Double = producto.precio * cantidad
}