package com.example.project_kotlin.entidades

import androidx.annotation.NonNull
import androidx.room.*
import java.util.UUID

@Entity(
    tableName = "movimiento_inventario",
    foreignKeys = [
        ForeignKey(entity = ProductoBase::class,
            parentColumns = ["id"],
            childColumns = ["productoBaseId"],
            onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("productoBaseId")]
)
data class MovimientoInventario(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val productoBaseId: String,
    val tipoMovimiento: String, // "ENTRADA" o "SALIDA"
    val cantidad: Double,
    val fecha: String, // formato YYYY-MM-DD
    val motivo: String // "compra", "venta automática", etc.
)

