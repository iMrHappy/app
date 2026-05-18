package com.example.bodegazegarrahuerta

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.bodegazegarrahuerta.viewmodel.CarritoViewModel

class BodegaApp : Application(), ViewModelStoreOwner {

    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }

    override val viewModelStore: ViewModelStore
        get() = appViewModelStore

    val carritoViewModel: CarritoViewModel by lazy {
        ViewModelProvider(this)[CarritoViewModel::class.java]
    }

}