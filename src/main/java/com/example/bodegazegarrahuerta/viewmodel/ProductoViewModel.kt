package com.example.bodegazegarrahuerta.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodegazegarrahuerta.model.Producto
import com.example.bodegazegarrahuerta.repository.ProductoRepository
import kotlinx.coroutines.launch

class ProductoViewModel(private val repo: ProductoRepository) : ViewModel() {

    private val _productos = MutableLiveData<List<Producto>>(emptyList())
    val productos: LiveData<List<Producto>> = _productos

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    fun cargar() {
        viewModelScope.launch {
            try {
                _productos.value = repo.obtenerTodos()
            } catch (e: Exception) {
                _mensaje.value = "Error al cargar: ${e.message}"
            }
        }
    }

    fun insertar(p: Producto) {
        viewModelScope.launch {
            try {
                repo.insertar(p)
                _mensaje.value = "Producto agregado"
                cargar()
            } catch (e: Exception) {
                _mensaje.value = "Error al agregar: ${e.message}"
            }
        }
    }

    fun actualizar(p: Producto) {
        viewModelScope.launch {
            try {
                repo.actualizar(p)
                _mensaje.value = "Producto actualizado"
                cargar()
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar: ${e.message}"
            }
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            try {
                repo.eliminar(id)
                _mensaje.value = "Producto eliminado"
                cargar()
            } catch (e: Exception) {
                _mensaje.value = "Error al eliminar: ${e.message}"
            }
        }
    }
}