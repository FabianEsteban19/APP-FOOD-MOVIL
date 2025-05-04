package com.example.project_kotlin.entidades

import androidx.annotation.NonNull
import androidx.room.*
import java.util.UUID

@Entity(tableName = "Producto_Base")
class ProductoBase(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    @NonNull @ColumnInfo(name = "cod_prodBase") var codProdBase: String,
    @NonNull @ColumnInfo(name = "nom_prodBase") var nomProdBase: String,
    @NonNull @ColumnInfo(name = "stock") var stock: Int,
) : java.io.Serializable {

    companion object {
        fun generarCodigo(listaProductos: List<ProductoBase>): String {
            if (listaProductos.isEmpty()) return "PB-001"

            // Suponiendo que los códigos están en el formato "PB-001", "PB-002", etc.
            val ultimoCodigo = listaProductos.maxByOrNull {
                it.codProdBase.split('-')[1].toIntOrNull() ?: 0
            }?.codProdBase ?: "PB-000"

            val numero = ultimoCodigo.split('-')[1].toInt() + 1

            return "PB-${String.format("%03d", numero)}"
        }
    }
}
