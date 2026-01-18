package edu.ucne.registrosapp.data.mappers

import edu.ucne.registrosapp.data.local.entities.EstudianteEntity
import edu.ucne.registrosapp.domain.models.Estudiante

fun EstudianteEntity.toDomain(): Estudiante {
    return Estudiante(
        estudianteId = this.estudianteId,
        nombres = this.nombres,
        email = this.email,
        edad = this.edad
    )
}

fun Estudiante.toEntity(): EstudianteEntity {
    return EstudianteEntity(
        estudianteId = this.estudianteId,
        nombres = this.nombres,
        email = this.email,
        edad = this.edad ?: 0
    )
}