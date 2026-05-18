package com.example.bodegazegarrahuerta.repository

import com.example.bodegazegarrahuerta.model.Producto
import com.example.bodegazegarrahuerta.network.RetrofitClient

class ProductoRepository {

    suspend fun obtenerTodos(): List<Producto> =
        RetrofitClient.api.getProductos()

    suspend fun insertar(producto: Producto) =
        RetrofitClient.api.crearProducto(producto)

    suspend fun actualizar(producto: Producto) =
        RetrofitClient.api.actualizarProducto(producto)

    suspend fun eliminar(id: Int) =
        RetrofitClient.api.eliminarProducto(id)
}