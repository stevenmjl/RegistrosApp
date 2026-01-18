package edu.ucne.registrosapp.domain.models

data class Estudiante(
    val estudianteId: Int? = null,
    val nombres: String = "",
    val email: String = "",
    val edad: Int? = null
)