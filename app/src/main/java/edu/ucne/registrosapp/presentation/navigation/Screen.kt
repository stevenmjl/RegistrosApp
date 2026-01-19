package edu.ucne.registrosapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object EstudianteList : Screen()

    @Serializable
    data class Estudiante(val estudianteId: Int) : Screen()
}