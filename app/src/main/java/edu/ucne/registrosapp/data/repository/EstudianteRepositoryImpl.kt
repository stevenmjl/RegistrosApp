package edu.ucne.registrosapp.data.repository

import edu.ucne.registrosapp.data.local.dao.EstudianteDao
import edu.ucne.registrosapp.data.mappers.toDomain
import edu.ucne.registrosapp.data.mappers.toEntity
import edu.ucne.registrosapp.domain.models.Estudiante
import edu.ucne.registrosapp.domain.repository.EstudianteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EstudianteRepositoryImpl @Inject constructor(
    private val dao: EstudianteDao
) : EstudianteRepository {

    override fun observeEstudiantes(): Flow<List<Estudiante>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getEstudiante(id: Int): Estudiante? = dao.getById(id)?.toDomain()

    override suspend fun upsert(estudiante: Estudiante): Int {
        dao.upsert(estudiante.toEntity())
        return estudiante.estudianteId ?: 0
    }

    override suspend fun delete(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun getByNombres(nombres: String): Estudiante? {
        val entity = dao.getByNombres(nombres)
        return entity?.toDomain()
    }
}