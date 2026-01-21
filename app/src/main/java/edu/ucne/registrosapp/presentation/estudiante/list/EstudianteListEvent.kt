package edu.ucne.registrosapp.presentation.estudiante.list

sealed interface EstudianteListEvent {
    data object Load : EstudianteListEvent
    data class Delete(val id: Int) : EstudianteListEvent
    data object CreateNew : EstudianteListEvent
    data class Edit(val id: Int) : EstudianteListEvent
    data class ShowMessage(val message: String) : EstudianteListEvent
}