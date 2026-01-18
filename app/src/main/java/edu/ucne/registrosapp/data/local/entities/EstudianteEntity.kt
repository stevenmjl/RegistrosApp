package edu.ucne.registrosapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudiantes")
data class EstudianteEntity(
    @PrimaryKey(autoGenerate = true)
    val estudianteId: Int? = null,
    val nombres: String,
    val email: String,
    val edad: Int
)