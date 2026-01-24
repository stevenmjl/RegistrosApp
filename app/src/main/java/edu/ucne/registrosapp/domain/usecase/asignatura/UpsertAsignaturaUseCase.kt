package edu.ucne.registrosapp.domain.usecase.asignatura

import edu.ucne.registrosapp.domain.models.Asignatura
import edu.ucne.registrosapp.domain.repository.AsignaturaRepository
import javax.inject.Inject

class UpsertAsignaturaUseCase @Inject constructor(
    private val repository: AsignaturaRepository
) {
    suspend operator fun invoke(asignatura: Asignatura): Result<Int> {

        val vCodigo = validateCodigo(asignatura.codigo)
        if (!vCodigo.isValid) return Result.failure(IllegalArgumentException(vCodigo.error))

        val vNombre = validateNombreAsignatura(asignatura.nombre)
        if (!vNombre.isValid) return Result.failure(IllegalArgumentException(vNombre.error))

        val vAula = validateAula(asignatura.aula)
        if (!vAula.isValid) return Result.failure(IllegalArgumentException(vAula.error))

        val vCreditos = validateCreditos(asignatura.creditos?.toString() ?: "")
        if (!vCreditos.isValid) return Result.failure(IllegalArgumentException(vCreditos.error))

        return runCatching { repository.upsert(asignatura) }
    }
}