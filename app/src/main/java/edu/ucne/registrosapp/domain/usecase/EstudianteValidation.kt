package edu.ucne.registrosapp.domain.usecase

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validateNombres(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "El nombre es requerido")
    if (value.length < 3) return ValidationResult(false, "Mínimo 3 caracteres")
    return ValidationResult(true)
}

fun validateEmail(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "El email es requerido")
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
        return ValidationResult(false, "Email no válido")
    }
    return ValidationResult(true)
}

fun validateEdad(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "La edad es requerida")
    val number = value.toIntOrNull() ?: return ValidationResult(false, "Debe ser un número")
    if (number <= 0 || number > 120) return ValidationResult(false, "Edad no válida")
    return ValidationResult(true)
}