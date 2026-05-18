package com.example.bodegazegarrahuerta.network

import com.example.bodegazegarrahuerta.model.Producto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @GET("productos.php")
    suspend fun getProductos(): List<Producto>

    @POST("productos.php")
    suspend fun crearProducto(@Body producto: Producto): Map<String, String>

    @PUT("productos.php")
    suspend fun actualizarProducto(@Body producto: Producto): Map<String, String>

    @DELETE("productos.php")
    suspend fun eliminarProducto(@Query("id") id: Int): Map<String, String>
}