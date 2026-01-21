package edu.ucne.registrosapp.presentation.estudiante.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrosapp.domain.usecase.DeleteEstudianteUseCase
import edu.ucne.registrosapp.domain.usecase.ObserveEstudiantesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstudianteListViewModel @Inject constructor(
    private val observeEstudiantesUseCase: ObserveEstudiantesUseCase,
    private val deleteEstudianteUseCase: DeleteEstudianteUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(EstudianteListUiState(isLoading = true))
    val state: StateFlow<EstudianteListUiState> = _state.asStateFlow()

    init {
        onEvent(EstudianteListEvent.Load)
    }

    fun onEvent(event: EstudianteListEvent) {
        when (event) {
            EstudianteListEvent.Load -> observe()
            is EstudianteListEvent.Delete -> onDelete(event.id)
            EstudianteListEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is EstudianteListEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is EstudianteListEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeEstudiantesUseCase().collectLatest { list ->
                _state.update { it.copy(isLoading = false, estudiantes = list, message = null) }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteEstudianteUseCase(id)
            onEvent(EstudianteListEvent.ShowMessage("Eliminado"))
        }
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateToCreate = false, navigateToEditId = null) }
    }
}