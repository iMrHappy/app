package com.example.bodegazegarrahuerta.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bodegazegarrahuerta.model.Pedido
import com.example.bodegazegarrahuerta.model.Producto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "bodega.db", null, 5) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE productos (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre    TEXT NOT NULL,
                categoria TEXT NOT NULL,
                precio    REAL NOT NULL,
                stock     INTEGER NOT NULL,
                imagen    TEXT DEFAULT ''
            )
        """)
        db.execSQL("""
            CREATE TABLE pedidos (
                id                INTEGER PRIMARY KEY AUTOINCREMENT,
                codigoSeguimiento TEXT NOT NULL,
                clienteNombre     TEXT,
                clienteTel        TEXT,
                clienteDir        TEXT,
                total             REAL,
                fecha             TEXT,
                status            TEXT DEFAULT 'Preparando pedido'
            )
        """)
        db.execSQL("""
            CREATE TABLE detalle_pedidos (
                id         INTEGER PRIMARY KEY AUTOINCREMENT,
                pedidoId   INTEGER,
                productoId INTEGER,
                cantidad   INTEGER,
                precio     REAL
            )
        """)
        insertarProductosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS detalle_pedidos")
        db.execSQL("DROP TABLE IF EXISTS pedidos")
        db.execSQL("DROP TABLE IF EXISTS productos")
        onCreate(db)
    }

    // ─── PRODUCTOS ───────────────────────────────────────────

    private fun insertarProductosIniciales(db: SQLiteDatabase) {
        val productos = listOf(
            arrayOf("Arroz Costeno 1kg",         "Abarrotes",  3.50, 100),
            arrayOf("Azucar Rubia 1kg",           "Abarrotes",  2.80, 80),
            arrayOf("Aceite Primor 1L",           "Abarrotes",  8.90, 60),
            arrayOf("Fideos Don Vittorio 500g",   "Abarrotes",  2.50, 90),
            arrayOf("Leche Gloria 400g",          "Abarrotes",  4.20, 70),
            arrayOf("Sal Marina 1kg",             "Abarrotes",  1.00, 120),
            arrayOf("Atun Florida 170g",          "Abarrotes",  3.80, 50),
            arrayOf("Harina Blanca Flor 1kg",     "Abarrotes",  3.20, 55),
            arrayOf("Avena Quaker 200g",          "Abarrotes",  2.90, 65),
            arrayOf("Cafe Kirma 200g",            "Abarrotes",  9.50, 40),
            arrayOf("Lejia Clorox 1L",            "Limpieza",   3.50, 80),
            arrayOf("Detergente Ariel 500g",      "Limpieza",   6.90, 60),
            arrayOf("Jabon Bolivar 250g",         "Limpieza",   2.00, 90),
            arrayOf("Suavitel 900ml",             "Limpieza",   7.50, 45),
            arrayOf("Limpiatodo Sapolio 500ml",   "Limpieza",   4.20, 55),
            arrayOf("Esponja Scotch Brite x2",    "Limpieza",   3.00, 70),
            arrayOf("Papel Higienico Elite x4",   "Limpieza",   5.50, 100),
            arrayOf("Vaso Descartable x25",       "Plasticos",  2.50, 80),
            arrayOf("Plato Descartable x10",      "Plasticos",  2.00, 70),
            arrayOf("Bolsa Negra x10",            "Plasticos",  1.50, 100),
            arrayOf("Cubiertos Descartables x10", "Plasticos",  2.80, 60),
            arrayOf("Contenedor c/tapa 500ml",    "Plasticos",  1.80, 90),
            arrayOf("Jarra Plastica 2L",          "Plasticos",  6.00, 40),
            arrayOf("Foco LED 9W",                "Ferreteria", 5.50, 50),
            arrayOf("Cinta Aislante 3M",          "Ferreteria", 3.00, 60),
            arrayOf("Pila AA Duracell x2",        "Ferreteria", 4.50, 80),
            arrayOf("Candado Forte 40mm",         "Ferreteria", 12.00, 30),
            arrayOf("Extension 3 tomas 1.5m",     "Ferreteria", 15.00, 25),
            arrayOf("Pegamento UHU 21g",          "Ferreteria", 4.00, 45),
            arrayOf("Pintura Spray Negro 400ml",  "Ferreteria", 9.00, 20),
            arrayOf("Destornillador Estrella",    "Ferreteria", 7.50, 35)
        )
        productos.forEach { p ->
            val cv = ContentValues().apply {
                put("nombre",    p[0] as String)
                put("categoria", p[1] as String)
                put("precio",    p[2] as Double)
                put("stock",     p[3] as Int)
                put("imagen",    "")
            }
            db.insert("productos", null, cv)
        }
    }

    fun obtenerProductos(): List<Producto> {
        val lista = mutableListOf<Producto>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM productos", null)
        while (cursor.moveToNext()) {
            lista.add(Producto(
                id        = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombre    = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                precio    = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                stock     = cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                imagen    = cursor.getString(cursor.getColumnIndexOrThrow("imagen"))
            ))
        }
        cursor.close()
        return lista
    }

    fun insertarProducto(p: Producto): Long {
        val cv = ContentValues().apply {
            put("nombre",    p.nombre)
            put("categoria", p.categoria)
            put("precio",    p.precio)
            put("stock",     p.stock)
            put("imagen",    p.imagen)
        }
        return writableDatabase.insert("productos", null, cv)
    }

    fun actualizarProducto(p: Producto): Int {
        val cv = ContentValues().apply {
            put("nombre",    p.nombre)
            put("categoria", p.categoria)
            put("precio",    p.precio)
            put("stock",     p.stock)
            put("imagen",    p.imagen)
        }
        return writableDatabase.update("productos", cv, "id=?", arrayOf(p.id.toString()))
    }

    fun eliminarProducto(id: Int): Int =
        writableDatabase.delete("productos", "id=?", arrayOf(id.toString()))

    // ─── PEDIDOS ─────────────────────────────────────────────

    fun generarCodigoSeguimiento(): String {
        val fecha = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val count = obtenerPedidos().size + 1
        return "BOD-%s-%03d".format(fecha, count)
    }

    fun insertarPedido(pedido: Pedido, detalles: List<Pair<Int, Int>>): Long {
        val db = writableDatabase
        db.beginTransaction()
        return try {
            val codigo = generarCodigoSeguimiento()
            val cv = ContentValues().apply {
                put("codigoSeguimiento", codigo)
                put("clienteNombre",     pedido.clienteNombre)
                put("clienteTel",        pedido.clienteTel)
                put("clienteDir",        pedido.clienteDir)
                put("total",             pedido.total)
                put("fecha",             pedido.fecha)
                put("status",            "Preparando pedido")
            }
            val pedidoId = db.insert("pedidos", null, cv)
            detalles.forEach { (productoId, cantidad) ->
                val precio = db.rawQuery(
                    "SELECT precio FROM productos WHERE id=?",
                    arrayOf(productoId.toString())
                ).use { c -> if (c.moveToFirst()) c.getDouble(0) else 0.0 }
                val dv = ContentValues().apply {
                    put("pedidoId",   pedidoId)
                    put("productoId", productoId)
                    put("cantidad",   cantidad)
                    put("precio",     precio)
                }
                db.insert("detalle_pedidos", null, dv)
            }
            db.setTransactionSuccessful()
            pedidoId
        } finally {
            db.endTransaction()
        }
    }

    fun obtenerPedidos(): List<Pedido> {
        val lista = mutableListOf<Pedido>()
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM pedidos ORDER BY id DESC", null
        )
        while (cursor.moveToNext()) {
            lista.add(Pedido(
                id                = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                codigoSeguimiento = cursor.getString(cursor.getColumnIndexOrThrow("codigoSeguimiento")),
                clienteNombre     = cursor.getString(cursor.getColumnIndexOrThrow("clienteNombre")),
                clienteTel        = cursor.getString(cursor.getColumnIndexOrThrow("clienteTel")),
                clienteDir        = cursor.getString(cursor.getColumnIndexOrThrow("clienteDir")),
                total             = cursor.getDouble(cursor.getColumnIndexOrThrow("total")),
                fecha             = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                status            = cursor.getString(cursor.getColumnIndexOrThrow("status"))
            ))
        }
        cursor.close()
        return lista
    }

    fun buscarPedidoPorCodigo(codigo: String): Pedido? {
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM pedidos WHERE codigoSeguimiento=?", arrayOf(codigo)
        )
        val pedido = if (cursor.moveToFirst()) {
            Pedido(
                id                = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                codigoSeguimiento = cursor.getString(cursor.getColumnIndexOrThrow("codigoSeguimiento")),
                clienteNombre     = cursor.getString(cursor.getColumnIndexOrThrow("clienteNombre")),
                clienteTel        = cursor.getString(cursor.getColumnIndexOrThrow("clienteTel")),
                clienteDir        = cursor.getString(cursor.getColumnIndexOrThrow("clienteDir")),
                total             = cursor.getDouble(cursor.getColumnIndexOrThrow("total")),
                fecha             = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                status            = cursor.getString(cursor.getColumnIndexOrThrow("status"))
            )
        } else null
        cursor.close()
        return pedido
    }

    fun actualizarStatusPedido(id: Int, nuevoStatus: String): Int {
        val cv = ContentValues().apply { put("status", nuevoStatus) }
        return writableDatabase.update("pedidos", cv, "id=?", arrayOf(id.toString()))
    }

    // ─── ADMIN ───────────────────────────────────────────────

    fun validarAdmin(usuario: String, password: String): Boolean =
        usuario == "admin" && password == "admin123"
}