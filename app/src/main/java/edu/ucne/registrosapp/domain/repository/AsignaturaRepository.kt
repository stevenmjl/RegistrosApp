package edu.ucne.registrosapp.domain.repository

import edu.ucne.registrosapp.domain.models.Asignatura
import kotlinx.coroutines.flow.Flow

interface AsignaturaRepository {
    fun observeAsignaturas(): Flow<List<Asignatura>>
    suspend fun getAsignatura(id: Int): Asignatura?
    suspend fun upsert(asignatura: Asignatura): Int
    suspend fun delete(id: Int)
    suspend fun getByNombre(nombre: String): Asignatura?
}