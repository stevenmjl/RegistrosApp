package edu.ucne.registrosapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrosapp.data.local.dao.EstudianteDao
import edu.ucne.registrosapp.data.local.dao.AsignaturaDao
import edu.ucne.registrosapp.data.local.entities.EstudianteEntity
import edu.ucne.registrosapp.data.local.entities.AsignaturaEntity

@Database(
    entities = [
        EstudianteEntity::class,
        AsignaturaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RegistrosDb : RoomDatabase() {
    abstract fun estudianteDao(): EstudianteDao
    abstract fun asignaturaDao(): AsignaturaDao
}