package edu.ucne.registrosapp.domain.usecase.estudiante

import edu.ucne.registrosapp.domain.repository.EstudianteRepository
import javax.inject.Inject

class DeleteEstudianteUseCase @Inject constructor(
    private val repository: EstudianteRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.delete(id)
    }
}