package com.example.bodegazegarrahuerta.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Emulador Android Studio
    private const val BASE_URL = "http://10.0.2.2/bodega_api/"

    // ← Si usas celular físico en la red UPN, cambia a:
    // private const val BASE_URL = "http://10.144.87.12/bodega_api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}