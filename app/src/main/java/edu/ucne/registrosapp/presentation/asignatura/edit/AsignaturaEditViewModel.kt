package edu.ucne.registrosapp.presentation.asignatura.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrosapp.domain.models.Asignatura
import edu.ucne.registrosapp.domain.usecase.asignatura.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AsignaturaEditViewModel @Inject constructor(
    private val getAsignaturaUseCase: GetAsignaturaUseCase,
    private val upsertAsignaturaUseCase: UpsertAsignaturaUseCase,
    private val deleteAsignaturaUseCase: DeleteAsignaturaUseCase,
    private val getAsignaturaByNombreUseCase: GetAsignaturaByNombreUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AsignaturaEditUiState())
    val state: StateFlow<AsignaturaEditUiState> = _state.asStateFlow()

    fun onEvent(event: AsignaturaEditUiEvent) {
        when (event) {
            is AsignaturaEditUiEvent.Load -> onLoad(event.id)
            is AsignaturaEditUiEvent.CodigoChanged -> _state.update {
                it.copy(codigo = event.codigo, codigoError = null)
            }
            is AsignaturaEditUiEvent.NombreChanged -> _state.update {
                it.copy(nombre = event.nombre, nombreError = null)
            }
            is AsignaturaEditUiEvent.AulaChanged -> _state.update {
                it.copy(aula = event.aula, aulaError = null)
            }
            is AsignaturaEditUiEvent.CreditosChanged -> _state.update {
                it.copy(creditos = event.creditos, creditosError = null)
            }
            AsignaturaEditUiEvent.Save -> onSave()
            AsignaturaEditUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, asignaturaId = null) }
            return
        }
        viewModelScope.launch {
            val asignatura = getAsignaturaUseCase(id)
            if (asignatura != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        asignaturaId = asignatura.asignaturaId,
                        codigo = asignatura.codigo,
                        nombre = asignatura.nombre,
                        aula = asignatura.aula,
                        creditos = asignatura.creditos.toString()
                    )
                }
            }
        }
    }

    private fun onSave() {
        // 1. Limpiar los espacios inmediatamente usando .trim()
        val codigoLimpio = state.value.codigo.trim()
        val nombreLimpio = state.value.nombre.trim()
        val aulaLimpia = state.value.aula.trim()
        val creditosStrLimpio = state.value.creditos.trim()

        // 2. Validaciones de formato usando los valores limpios
        val vCodigo = validateCodigo(codigoLimpio)
        val vNombre = validateNombreAsignatura(nombreLimpio)
        val vAula = validateAula(aulaLimpia)
        val vCreditos = validateCreditos(creditosStrLimpio)

        if (!vCodigo.isValid || !vNombre.isValid || !vAula.isValid || !vCreditos.isValid) {
            _state.update {
                it.copy(
                    codigoError = vCodigo.error,
                    nombreError = vNombre.error,
                    aulaError = vAula.error,
                    creditosError = vCreditos.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            // 3. Validación de nombre único con el nombre ya LIMPIO
            val asignaturaExistente = getAsignaturaByNombreUseCase(nombreLimpio)

            if (asignaturaExistente != null && asignaturaExistente.asignaturaId != state.value.asignaturaId) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreError = "Ya existe una asignatura con este nombre."
                    )
                }
                return@launch
            }

            // 4. Crear el objeto Asignatura con datos limpios para guardar
            val asignatura = Asignatura(
                asignaturaId = state.value.asignaturaId,
                codigo = codigoLimpio,
                nombre = nombreLimpio,
                aula = aulaLimpia,
                creditos = creditosStrLimpio.toIntOrNull() ?: 0
            )

            val result = upsertAsignaturaUseCase(asignatura)
            result.onSuccess { newId ->
                _state.update { it.copy(isSaving = false, saved = true, asignaturaId = newId) }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.asignaturaId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteAsignaturaUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}