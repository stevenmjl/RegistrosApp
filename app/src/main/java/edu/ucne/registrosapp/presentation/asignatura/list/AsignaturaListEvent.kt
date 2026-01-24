package edu.ucne.registrosapp.presentation.asignatura.list

sealed interface AsignaturaListEvent {
    data object Load : AsignaturaListEvent
    data class Delete(val id: Int) : AsignaturaListEvent
    data object CreateNew : AsignaturaListEvent
    data class Edit(val id: Int) : AsignaturaListEvent
    data class ShowMessage(val message: String) : AsignaturaListEvent
}