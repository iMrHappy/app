package com.example.bodegazegarrahuerta.model

data class Pedido(
    val id: Int = 0,
    val codigoSeguimiento: String = "",
    val clienteNombre: String = "",
    val clienteTel: String = "",
    val clienteDir: String = "",
    val total: Double = 0.0,
    val fecha: String = "",
    val status: String = "Preparando pedido"
)