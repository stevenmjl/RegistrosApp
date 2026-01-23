package edu.ucne.registrosapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrosapp.data.local.entities.AsignaturaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AsignaturaDao {
    @Query("SELECT * FROM asignaturas ORDER BY asignaturaId DESC")
    fun observeAll(): Flow<List<AsignaturaEntity>>

    @Query("SELECT * FROM asignaturas WHERE asignaturaId = :id")
    suspend fun getById(id: Int): AsignaturaEntity?

    @Upsert
    suspend fun upsert(entity: AsignaturaEntity)

    @Query("DELETE FROM asignaturas WHERE asignaturaId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM asignaturas WHERE nombre = :nombre COLLATE NOCASE LIMIT 1")
    suspend fun getByNombre(nombre: String): AsignaturaEntity?
}