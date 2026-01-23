package edu.ucne.registrosapp.domain.models

data class Asignatura(
    val asignaturaId: Int? = null,
    val codigo: String = "",
    val nombre: String = "",
    val aula: String = "",
    val creditos: Int? = null
)