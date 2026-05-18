package com.example.bodegazegarrahuerta.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bodegazegarrahuerta.model.ItemCarrito
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "carrito")

class CarritoRepository(private val context: Context) {

    private val gson = Gson()
    private val key = stringPreferencesKey("items_carrito")

    // Flow que emite la lista de items (observable)
    val itemsFlow: Flow<List<ItemCarrito>> = context.dataStore.data.map { preferences ->
        val json = preferences[key] ?: return@map emptyList()
        val type = object : TypeToken<List<ItemCarrito>>() {}.type
        gson.fromJson<List<ItemCarrito>>(json, type) ?: emptyList()
    }

    // Guardar lista completa
    suspend fun saveItems(items: List<ItemCarrito>) {
        val json = gson.toJson(items)
        context.dataStore.edit { preferences ->
            preferences[key] = json
        }
    }

    // Limpiar carrito
    suspend fun clearItems() {
        context.dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }
}