package edu.ucne.registrosapp.domain.usecase.estudiante

import edu.ucne.registrosapp.domain.models.Estudiante
import edu.ucne.registrosapp.domain.repository.EstudianteRepository
import javax.inject.Inject

class UpsertEstudianteUseCase @Inject constructor(
    private val repository: EstudianteRepository
) {
    suspend operator fun invoke(estudiante: Estudiante): Result<Int> {

        val vNombres = validateNombres(estudiante.nombres)
        if (!vNombres.isValid) return Result.failure(IllegalArgumentException(vNombres.error))

        val vEmail = validateEmail(estudiante.email)
        if (!vEmail.isValid) return Result.failure(IllegalArgumentException(vEmail.error))

        val vEdad = validateEdad(estudiante.edad?.toString() ?: "")
        if (!vEdad.isValid) return Result.failure(IllegalArgumentException(vEdad.error))

        return runCatching { repository.upsert(estudiante) }
    }
}