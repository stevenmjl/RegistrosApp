package edu.ucne.registrosapp.presentation.asignatura.list

import edu.ucne.registrosapp.domain.models.Asignatura

data class AsignaturaListUiState(
    val isLoading: Boolean = false,
    val asignaturas: List<Asignatura> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)