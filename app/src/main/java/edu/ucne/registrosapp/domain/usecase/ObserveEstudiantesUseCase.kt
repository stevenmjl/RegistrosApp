package edu.ucne.registrosapp.domain.usecase

import edu.ucne.registrosapp.domain.models.Estudiante
import edu.ucne.registrosapp.domain.repository.EstudianteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveEstudiantesUseCase @Inject constructor(
    private val repository: EstudianteRepository
) {
    operator fun invoke(): Flow<List<Estudiante>> = repository.observeEstudiantes()
}