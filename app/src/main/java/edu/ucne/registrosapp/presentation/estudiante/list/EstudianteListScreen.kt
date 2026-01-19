package edu.ucne.registrosapp.presentation.estudiante.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrosapp.domain.models.Estudiante
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import edu.ucne.registrosapp.presentation.components.ConfirmDeleteDialog

@Composable
fun EstudianteListScreen(
    viewModel: EstudianteListViewModel = hiltViewModel(),
    onDrawer: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Lógica del Diálogo usando el componente nuevo
    var showDeleteDialog by remember { mutableStateOf(false) }
    var estudianteIdToDelete by remember { mutableIntStateOf(0) }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.onEvent(EstudianteListEvent.Delete(estudianteIdToDelete))
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            onNavigateToCreate()
            viewModel.onNavigationHandled()
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            onNavigateToEdit(id)
            viewModel.onNavigationHandled()
        }
    }

    EstudianteListBody(
        state = state,
        onDrawer = onDrawer,
        onEvent = { event ->
            // Si el evento es borrar, interceptamos para mostrar el diálogo
            if (event is EstudianteListEvent.Delete) {
                estudianteIdToDelete = event.id
                showDeleteDialog = true
            } else {
                viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EstudianteListBody(
    state: EstudianteListUiState,
    onDrawer: () -> Unit,
    onEvent: (EstudianteListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Estudiantes") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(EstudianteListEvent.CreateNew) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (state.estudiantes.isEmpty() && !state.isLoading) {
                Text(
                    "No hay estudiantes registrados.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item { Spacer(Modifier.height(8.dp)) }
                items(state.estudiantes) { estudiante ->
                    EstudianteCard(
                        estudiante = estudiante,
                        onClick = { onEvent(EstudianteListEvent.Edit(estudiante.estudianteId ?: 0)) },
                        onDelete = { onEvent(EstudianteListEvent.Delete(estudiante.estudianteId ?: 0)) }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) } // Espacio para que el FAB no tape el último item
            }
        }
    }
}

@Composable
private fun EstudianteCard(
    estudiante: Estudiante,
    onClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(estudiante.estudianteId ?: 0) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = estudiante.nombres,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(
                    text = estudiante.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${estudiante.edad} años",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(onClick = { onClick(estudiante.estudianteId ?: 0) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = { onDelete(estudiante.estudianteId ?: 0) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview
@Composable
private fun EstudianteListBodyPreview() {
    MaterialTheme {
        val state = EstudianteListUiState()
        EstudianteListBody(state, onDrawer = {}, onEvent = {})
    }
}