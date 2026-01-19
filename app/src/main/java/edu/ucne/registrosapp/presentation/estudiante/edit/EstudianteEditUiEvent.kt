package edu.ucne.registrosapp.presentation.estudiante.edit

sealed interface EstudianteEditUiEvent {
    data class Load(val id: Int?) : EstudianteEditUiEvent
    data class NombresChanged(val nombres: String) : EstudianteEditUiEvent
    data class EmailChanged(val email: String) : EstudianteEditUiEvent
    data class EdadChanged(val edad: String) : EstudianteEditUiEvent
    data object Save : EstudianteEditUiEvent
    data object Delete : EstudianteEditUiEvent
}