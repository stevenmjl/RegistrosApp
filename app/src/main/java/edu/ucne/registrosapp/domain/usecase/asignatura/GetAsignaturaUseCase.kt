package edu.ucne.registrosapp.domain.usecase.asignatura

import edu.ucne.registrosapp.domain.models.Asignatura
import edu.ucne.registrosapp.domain.repository.AsignaturaRepository
import javax.inject.Inject

class GetAsignaturaUseCase @Inject constructor(
    private val repository: AsignaturaRepository
) {
    suspend operator fun invoke(id: Int): Asignatura? {
        return repository.getAsignatura(id)
    }
}