package com.example.project_kotlin.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_kotlin.dao.*
import com.example.project_kotlin.entidades.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(
    entities = [Caja::class,
        Cargo::class, Comanda::class,
        Comprobante::class, Usuario::class,
        CategoriaPlato::class, Mesa::class,
        DetalleComanda::class,
        Empleado::class, Plato::class, Establecimiento::class,
        EstadoComanda::class, MetodoPago::class, TipoComprobante::class],
    version = 1,
    exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ComandaDatabase : RoomDatabase() {
    abstract fun cajaDao() : CajaDao
    abstract fun cargoDao() : CargoDao
    abstract fun comandaDao() : ComandaDao
    abstract fun comprobanteDao() : ComprobanteDao
    abstract fun usuarioDao() : UsuarioDao
    abstract fun categoriaPlatoDao() : CategoriaPlatoDao
    abstract fun mesaDao() : MesaDao

    //Entidades
    abstract fun metodoPagoDao() : MetodoPagoDao
    abstract fun tipoComprobanteDao() : TipoComprobanteDao
    abstract fun estadoComandaDao(): EstadoComandaDao
    abstract fun establecimientoDao() : EstablecimientoDao
    abstract fun platoDao() : PlatoDao
    abstract fun empleadoDao() : EmpleadoDao
    abstract fun detalleComandaDao() : DetalleComandaDao

    companion object {
        @Volatile
        private var INSTANCIA: ComandaDatabase? = null

        fun obtenerBaseDatos(context: Context): ComandaDatabase {
            return INSTANCIA ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ComandaDatabase::class.java,
                    "burger_house_app"
                ) .addCallback(object : RoomDatabase.Callback() {
                    @Override
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        CoroutineScope(Dispatchers.IO).launch() {
                            val instancia = obtenerBaseDatos(context)
                            //Al crear ingresar datos
                            val estadosComandaDao = instancia?.estadoComandaDao()
                            val metodosPagoDao = instancia?.metodoPagoDao()
                            val categoriaPlatoDao = instancia?.categoriaPlatoDao()
                            val platoDao = instancia?.platoDao()
                            val establecimientoDao = instancia?.establecimientoDao()
                            val tipoComprobanteDao = instancia?.tipoComprobanteDao()
                            val empleadoDao = instancia?.empleadoDao()
                            val usuarioDao = instancia?.usuarioDao()
                            val cargoDao = obtenerBaseDatos(context).cargoDao()

                            //Agregando cargos
                            cargoDao.guardar(Cargo(cargo= "ADMINISTRADOR"))
                            cargoDao.guardar(Cargo(cargo = "MESERO"))
                            cargoDao.guardar(Cargo(cargo= "CAJERO"))
                            cargoDao.guardar(Cargo(cargo= "COCINA"))
                            /*bdFirebase.child("cargo").child("1").setValue(CargoNoSql("ADMINISTRADOR"))
                            bdFirebase.child("cargo").child("2").setValue(CargoNoSql("MESERO"))
                            bdFirebase.child("cargo").child("3").setValue(CargoNoSql("CAJERO"))
                            bdFirebase.child("cargo").child("4").setValue(CargoNoSql("GERENTE"))*/

                            //Agregando estados
                            estadosComandaDao?.guardar(EstadoComanda(estadoComanda ="Creada"))
                            estadosComandaDao?.guardar(EstadoComanda(estadoComanda ="Confirmada"))
                            estadosComandaDao?.guardar(EstadoComanda(estadoComanda= "Pendiente"))
                            estadosComandaDao?.guardar(EstadoComanda(estadoComanda= "Pagada"))

                            //Métodos de pago
                            metodosPagoDao?.registrar(MetodoPago(nombreMetodoPago =  "Efectivo"))
                            metodosPagoDao?.registrar(MetodoPago(nombreMetodoPago =  "Transferencia"))
                            metodosPagoDao?.registrar(MetodoPago(nombreMetodoPago =  "APP QR"))


                            //Categoría Plato

                            categoriaPlatoDao?.guardar(CategoriaPlato("C-001", "Bebidas"))
                            categoriaPlatoDao?.guardar(CategoriaPlato("C-002", "Hamburguesas"))
                            categoriaPlatoDao?.guardar(CategoriaPlato("C-003", "Adicionales"))
                            categoriaPlatoDao?.guardar(CategoriaPlato("C-004", "Salchipapas"))
                            categoriaPlatoDao?.guardar(CategoriaPlato("C-005", "Alitas"))


                            //Establecimiento
                            establecimientoDao?.guardar(
                                Establecimiento(1, "Burger House",
                                "991068482", "Alameda del Corregidor 3342", "00000000000")
                            )
                            //Tipo comprobante
                            tipoComprobanteDao?.guardar(TipoComprobante(tipo = "Boleta"))

                            //FECHA PARA EMPLEADO
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                            val fechaActual = Date()
                            val fechaFormateada = dateFormat.format(fechaActual)

                            //CREAR EMPLEADOS
                            val usuario = Usuario(correo= "jaeg2025@burgerHouse.com", contrasena = "jose2025")
                            usuarioDao?.guardar(usuario)
                            val empleado = Empleado(nombreEmpleado = "Jose", apellidoEmpleado = "Antonio", telefonoEmpleado = "991058757",
                                    dniEmpleado = "10166573", fechaRegistro = fechaFormateada, cargo_id = 1, usuario_id = 1)
                            empleadoDao?.guardar(empleado)


                        }
                    }
                })
                    .build()
                    .also { INSTANCIA = it }
            }
        }
    }
}

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}