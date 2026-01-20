package edu.ucne.registrosapp.presentation.estudiante.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrosapp.domain.models.Estudiante
import edu.ucne.registrosapp.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstudianteEditViewModel @Inject constructor(
    private val getEstudianteUseCase: GetEstudianteUseCase,
    private val upsertEstudianteUseCase: UpsertEstudianteUseCase,
    private val deleteEstudianteUseCase: DeleteEstudianteUseCase,
    private val getEstudianteByNombresUseCase: GetEstudianteByNombresUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EstudianteEditUiState())
    val state: StateFlow<EstudianteEditUiState> = _state.asStateFlow()

    fun onEvent(event: EstudianteEditUiEvent) {
        when (event) {
            is EstudianteEditUiEvent.Load -> onLoad(event.id)
            is EstudianteEditUiEvent.NombresChanged -> _state.update {
                it.copy(nombres = event.nombres, nombresError = null)
            }
            is EstudianteEditUiEvent.EmailChanged -> _state.update {
                it.copy(email = event.email, emailError = null)
            }
            is EstudianteEditUiEvent.EdadChanged -> _state.update {
                it.copy(edad = event.edad, edadError = null)
            }
            EstudianteEditUiEvent.Save -> onSave()
            EstudianteEditUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, estudianteId = null) }
            return
        }
        viewModelScope.launch {
            val estudiante = getEstudianteUseCase(id)
            if (estudiante != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        estudianteId = estudiante.estudianteId,
                        nombres = estudiante.nombres,
                        email = estudiante.email,
                        edad = estudiante.edad.toString()
                    )
                }
            }
        }
    }

    private fun onSave() {
        // 1. Limpiar los espacios inmediatamente usando .trim()
        val nombresLimpio = state.value.nombres.trim()
        val emailLimpio = state.value.email.trim()
        val edadStrLimpio = state.value.edad.trim()

        // 2. Validaciones de formato usando los valores limpios
        val vNombres = validateNombres(nombresLimpio)
        val vEmail = validateEmail(emailLimpio)
        val vEdad = validateEdad(edadStrLimpio)

        if (!vNombres.isValid || !vEmail.isValid || !vEdad.isValid) {
            _state.update {
                it.copy(
                    nombresError = vNombres.error,
                    emailError = vEmail.error,
                    edadError = vEdad.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            // 3. Validación de nombre único con el nombre ya LIMPIO
            val estudianteExistente = getEstudianteByNombresUseCase(nombresLimpio)

            // Si el nombre ya existe y no es el mismo que estamos editando
            if (estudianteExistente != null && estudianteExistente.estudianteId != state.value.estudianteId) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombresError = "Ya existe un estudiante con este nombre."
                    )
                }
                return@launch
            }

            // 4. Crear el objeto Estudiante con datos limpios para guardar
            val estudiante = Estudiante(
                estudianteId = state.value.estudianteId,
                nombres = nombresLimpio,
                email = emailLimpio,
                edad = edadStrLimpio.toIntOrNull() ?: 0
            )

            val result = upsertEstudianteUseCase(estudiante)
            result.onSuccess { newId ->
                _state.update { it.copy(isSaving = false, saved = true, estudianteId = newId) }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }
    private fun onDelete() {
        val id = state.value.estudianteId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteEstudianteUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}