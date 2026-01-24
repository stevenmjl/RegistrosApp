package edu.ucne.registrosapp.presentation.asignatura.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrosapp.domain.usecase.asignatura.DeleteAsignaturaUseCase
import edu.ucne.registrosapp.domain.usecase.asignatura.ObserveAsignaturasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AsignaturaListViewModel @Inject constructor(
    private val observeAsignaturasUseCase: ObserveAsignaturasUseCase,
    private val deleteAsignaturaUseCase: DeleteAsignaturaUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AsignaturaListUiState(isLoading = true))
    val state: StateFlow<AsignaturaListUiState> = _state.asStateFlow()

    init {
        onEvent(AsignaturaListEvent.Load)
    }

    fun onEvent(event: AsignaturaListEvent) {
        when (event) {
            AsignaturaListEvent.Load -> observe()
            is AsignaturaListEvent.Delete -> onDelete(event.id)
            AsignaturaListEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is AsignaturaListEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is AsignaturaListEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeAsignaturasUseCase().collectLatest { list ->
                _state.update { it.copy(isLoading = false, asignaturas = list, message = null) }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteAsignaturaUseCase(id)
            onEvent(AsignaturaListEvent.ShowMessage("Asignatura eliminada"))
        }
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateToCreate = false, navigateToEditId = null) }
    }
}