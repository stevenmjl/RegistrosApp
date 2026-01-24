package edu.ucne.registrosapp.data.repository

import edu.ucne.registrosapp.data.local.dao.AsignaturaDao
import edu.ucne.registrosapp.data.mappers.toDomain
import edu.ucne.registrosapp.data.mappers.toEntity
import edu.ucne.registrosapp.domain.models.Asignatura
import edu.ucne.registrosapp.domain.repository.AsignaturaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AsignaturaRepositoryImpl @Inject constructor(
    private val dao: AsignaturaDao
) : AsignaturaRepository {

    override fun observeAsignaturas(): Flow<List<Asignatura>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getAsignatura(id: Int): Asignatura? = dao.getById(id)?.toDomain()

    override suspend fun upsert(asignatura: Asignatura): Int {
        dao.upsert(asignatura.toEntity())
        return asignatura.asignaturaId ?: 0
    }

    override suspend fun delete(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun getByNombre(nombre: String): Asignatura? {
        val entity = dao.getByNombre(nombre)
        return entity?.toDomain()
    }
}