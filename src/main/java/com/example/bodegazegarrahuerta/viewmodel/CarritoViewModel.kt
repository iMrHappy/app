package com.example.bodegazegarrahuerta.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bodegazegarrahuerta.model.ItemCarrito
import com.example.bodegazegarrahuerta.model.Producto
import com.example.bodegazegarrahuerta.repository.CarritoRepository
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CarritoRepository(application.applicationContext)

    // Lista mutable en memoria para operaciones rápidas
    private var _itemsList = mutableListOf<ItemCarrito>()

    // LiveData para la UI
    private val _items = MutableLiveData<MutableList<ItemCarrito>>(mutableListOf())
    val items: LiveData<MutableList<ItemCarrito>> get() = _items

    init {
        // Cargar el carrito guardado al iniciar el ViewModel
        viewModelScope.launch {
            repository.itemsFlow.collect { savedItems ->
                _itemsList = savedItems.toMutableList()
                _items.value = _itemsList
            }
        }
    }

    private fun persist() {
        viewModelScope.launch {
            repository.saveItems(_itemsList)
        }
    }

    fun agregar(producto: Producto) {
        val existente = _itemsList.find { it.producto.id == producto.id }
        if (existente != null) {
            existente.cantidad++
        } else {
            _itemsList.add(ItemCarrito(producto))
        }
        _items.value = _itemsList
        persist()
    }

    fun eliminar(item: ItemCarrito) {
        _itemsList.remove(item)
        _items.value = _itemsList
        persist()
    }

    fun limpiar() {
        _itemsList.clear()
        _items.value = _itemsList
        viewModelScope.launch {
            repository.clearItems()
        }
    }

    fun total(): Double = _itemsList.sumOf { it.subtotal() }
}