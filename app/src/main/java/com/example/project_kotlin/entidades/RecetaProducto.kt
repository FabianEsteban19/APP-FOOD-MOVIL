package com.example.project_kotlin.entidades

import androidx.annotation.NonNull
import androidx.room.*
import java.util.UUID

@Entity(
    tableName = "receta_plato",
    primaryKeys = ["plato_id", "producto_base_id"],
    foreignKeys = [
        ForeignKey(
            entity = Plato::class,
            parentColumns = ["id"],
            childColumns = ["plato_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductoBase::class,
            parentColumns = ["producto_base_id"], // ✔ clave primaria real
            childColumns = ["producto_base_id"],   // ✔ nueva columna hija
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("plato_id"), Index("producto_base_id")]
)
data class RecetaPlato(
    @ColumnInfo(name = "plato_id") val platoId: String,
    @ColumnInfo(name = "producto_base_id") val productoBaseId: UUID, // ✔ UUID como en ProductoBase
    @ColumnInfo(name = "cantidad_utilizada") val cantidadUtilizada: Double
) : java.io.Serializable



