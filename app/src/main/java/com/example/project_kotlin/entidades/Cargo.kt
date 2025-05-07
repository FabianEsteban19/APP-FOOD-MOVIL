package com.example.project_kotlin.entidades

import androidx.room.*
import java.util.UUID

@Entity(
    tableName = "cargo",
    indices = [Index(value = ["codCargo"], unique = true)]
)
data class Cargo(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @ColumnInfo (name = "codCargo") var codCargo: Int,
    @ColumnInfo (name = "nomCargo") var cargo:String ):java.io.Serializable {
}