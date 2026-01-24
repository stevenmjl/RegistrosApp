package edu.ucne.registrosapp.domain.usecase.asignatura

import edu.ucne.registrosapp.domain.models.Asignatura
import edu.ucne.registrosapp.domain.repository.AsignaturaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAsignaturasUseCase @Inject constructor(
    private val repository: AsignaturaRepository
) {
    operator fun invoke(): Flow<List<Asignatura>> = repository.observeAsignaturas()
}