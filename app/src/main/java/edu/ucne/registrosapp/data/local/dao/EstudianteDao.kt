package edu.ucne.registrosapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrosapp.data.local.entities.EstudianteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EstudianteDao {
    @Query("SELECT * FROM estudiantes ORDER BY estudianteId DESC")
    fun observeAll(): Flow<List<EstudianteEntity>>

    @Query("SELECT * FROM estudiantes WHERE estudianteId = :id")
    suspend fun getById(id: Int): EstudianteEntity?

    @Upsert
    suspend fun upsert(entity: EstudianteEntity)

    @Delete
    suspend fun delete(entity: EstudianteEntity)

    @Query("DELETE FROM estudiantes WHERE estudianteId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM Estudiantes WHERE nombres = :nombres LIMIT 1")
    suspend fun getByNombres(nombres: String): EstudianteEntity?
}