package edu.ucne.registrosapp.data.mappers

import edu.ucne.registrosapp.data.local.entities.AsignaturaEntity
import edu.ucne.registrosapp.domain.models.Asignatura

fun AsignaturaEntity.toDomain(): Asignatura {
    return Asignatura(
        asignaturaId = this.asignaturaId,
        codigo = this.codigo,
        nombre = this.nombre,
        aula = this.aula,
        creditos = this.creditos
    )
}

fun Asignatura.toEntity(): AsignaturaEntity {
    return AsignaturaEntity(
        asignaturaId = this.asignaturaId,
        codigo = this.codigo,
        nombre = this.nombre,
        aula = this.aula,
        creditos = this.creditos ?: 0
    )
}