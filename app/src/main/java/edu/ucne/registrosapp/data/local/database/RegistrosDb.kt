package edu.ucne.registrosapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrosapp.data.local.dao.EstudianteDao
import edu.ucne.registrosapp.data.local.entities.EstudianteEntity

@Database(entities = [EstudianteEntity::class], version = 1, exportSchema = false)
abstract class RegistrosDb : RoomDatabase() {
    abstract fun estudianteDao(): EstudianteDao
}