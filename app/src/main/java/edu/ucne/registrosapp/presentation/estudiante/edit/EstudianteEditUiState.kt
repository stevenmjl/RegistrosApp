package edu.ucne.registrosapp.presentation.estudiante.edit

data class EstudianteEditUiState(
    val estudianteId: Int? = null,
    val nombres: String = "",
    val email: String = "",
    val edad: String = "",
    val nombresError: String? = null,
    val emailError: String? = null,
    val edadError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)