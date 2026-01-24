package edu.ucne.registrosapp.presentation.asignatura.edit

sealed interface AsignaturaEditUiEvent {
    data class Load(val id: Int?) : AsignaturaEditUiEvent
    data class CodigoChanged(val codigo: String) : AsignaturaEditUiEvent
    data class NombreChanged(val nombre: String) : AsignaturaEditUiEvent
    data class AulaChanged(val aula: String) : AsignaturaEditUiEvent
    data class CreditosChanged(val creditos: String) : AsignaturaEditUiEvent
    data object Save : AsignaturaEditUiEvent
    data object Delete : AsignaturaEditUiEvent
}