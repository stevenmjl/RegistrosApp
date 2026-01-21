package edu.ucne.registrosapp.presentation.estudiante.list

import edu.ucne.registrosapp.domain.models.Estudiante

data class EstudianteListUiState(
    val isLoading: Boolean = false,
    val estudiantes: List<Estudiante> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)