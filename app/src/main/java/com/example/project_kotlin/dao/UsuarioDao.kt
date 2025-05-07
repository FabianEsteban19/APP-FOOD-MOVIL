package com.example.project_kotlin.dao

import androidx.room.*
import com.example.project_kotlin.entidades.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario")
     fun obtenerTodo() : List<Usuario>

    @Query("SELECT last_insert_rowid() FROM usuario")
    fun obtenerUltimoId(): Long

    @Query("SELECT * FROM usuario WHERE id = :id")
     fun obtenerPorId(id: Long) : Usuario

    @Query("SELECT * FROM usuario WHERE usuario = :user")
    fun obtenerPorUsuario(user: String) : Usuario

    @Query("SELECT * FROM usuario WHERE usuario = :usuario AND contraseña = :contrasenia")
    fun verificarUsuarioYContrasenia(usuario: String, contrasenia:String):Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun guardar(usuario: Usuario) : Long

    @Update
     fun actualizar(usuario: Usuario)

    @Delete
     fun eliminar(usuario: Usuario)
}