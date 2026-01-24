package edu.ucne.registrosapp.domain.usecase.asignatura

import edu.ucne.registrosapp.domain.repository.AsignaturaRepository
import javax.inject.Inject

class DeleteAsignaturaUseCase @Inject constructor(
    private val repository: AsignaturaRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.delete(id)
    }
}