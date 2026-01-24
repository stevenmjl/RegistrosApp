package edu.ucne.registrosapp.domain.usecase.estudiante

import edu.ucne.registrosapp.domain.models.Estudiante
import edu.ucne.registrosapp.domain.repository.EstudianteRepository
import javax.inject.Inject

class GetEstudianteUseCase @Inject constructor(
    private val repository: EstudianteRepository
) {
    suspend operator fun invoke(id: Int): Estudiante? {
        return repository.getEstudiante(id)
    }
}