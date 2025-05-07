package com.example.project_kotlin.entidades.relaciones

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.project_kotlin.entidades.Plato
import com.example.project_kotlin.entidades.ProductoBase
import com.example.project_kotlin.entidades.RecetaPlato

class PlatoConIngredientes (
    @Embedded val plato: Plato,
    @Relation(
        parentColumn = "id",
        entityColumn = "plato_id",
        associateBy = Junction(
            value = RecetaPlato::class,
            parentColumn = "plato_id",
            entityColumn = "cod_prodBase"
        )
    )
    val ingredientes : List<ProductoBase>
)