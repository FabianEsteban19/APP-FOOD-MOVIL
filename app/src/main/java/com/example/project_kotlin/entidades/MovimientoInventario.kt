package com.example.project_kotlin.entidades

import androidx.annotation.NonNull
import androidx.room.*
import java.util.UUID

@Entity(
    tableName = "movimiento_inventario",
    foreignKeys = [
        ForeignKey(
            entity = ProductoBase::class,
            parentColumns = ["producto_base_id"],  // ✅ nombre correcto
            childColumns = ["producto_base_id"],   // ✅ mismo nombre aquí
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("producto_base_id")]
)
data class MovimientoInventario(
    @PrimaryKey @ColumnInfo(name = "codMovInventario") val codMovInventario: String = "MOV-${UUID.randomUUID()}",
    @ColumnInfo(name = "producto_base_id") val productoBaseId: UUID,  // ✅ tipo UUID para coincidir con ProductoBase
    @ColumnInfo(name = "tipoMovimiento") val tipoMovimiento: String,  // "ENTRADA" o "SALIDA"
    @ColumnInfo(name = "cantidad") val cantidad: Double,
    @ColumnInfo(name = "fecha") val fecha: String,
    @ColumnInfo(name = "motivo") val motivo: String
)


