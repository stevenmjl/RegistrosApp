package edu.ucne.registrosapp.domain.usecase.asignatura

import edu.ucne.registrosapp.domain.usecase.estudiante.ValidationResult

fun validateCodigo(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "El código es requerido")
    return ValidationResult(true)
}

fun validateNombreAsignatura(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "El nombre es requerido")
    if (value.length < 3) return ValidationResult(false, "Mínimo 3 caracteres")
    return ValidationResult(true)
}

fun validateAula(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "El aula es requerida")
    return ValidationResult(true)
}

fun validateCreditos(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "Los créditos son requeridos")
    val number = value.toIntOrNull() ?: return ValidationResult(false, "Debe ser un número")
    if (number <= 0) return ValidationResult(false, "Debe ser mayor a 0")
    return ValidationResult(true)
}