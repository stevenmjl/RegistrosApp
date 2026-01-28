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
import androidx.compose.ui.text.font.FontWeight
import edu.ucne.registrosapp.presentation.components.ConfirmDeleteDialog

@Composable
fun EstudianteListScreen(
    onDrawer: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    viewModel: EstudianteListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EstudianteListBody(
        state = state,
        onEvent = viewModel::onEvent,
        onDrawer = onDrawer,
        onNavigateToCreate = onNavigateToCreate,
        onNavigateToEdit = onNavigateToEdit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EstudianteListBody(
    state: EstudianteListUiState,
    onEvent: (EstudianteListEvent) -> Unit,
    onDrawer: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var estudianteIdToDelete by remember { mutableIntStateOf(0) }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                onEvent(EstudianteListEvent.Delete(estudianteIdToDelete))
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

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
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.estudiantes.isEmpty()) {
                Text(
                    text = "No hay estudiantes registrados.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }
                    items(state.estudiantes) { estudiante ->
                        EstudianteCard(
                            estudiante = estudiante,
                            onEdit = { onNavigateToEdit(estudiante.estudianteId ?: 0) },
                            onDelete = {
                                estudianteIdToDelete = estudiante.estudianteId ?: 0
                                showDeleteDialog = true
                            }
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun EstudianteCard(
    estudiante: Estudiante,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onEdit() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = estudiante.nombres,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
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

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, "Editar", tint = MaterialTheme.colorScheme.primary)
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true, name = "Lista con Estudiantes")
@Composable
private fun EstudianteListWithDataPreview() {
    MaterialTheme {
        EstudianteListBody(
            state = EstudianteListUiState(
                isLoading = false,
                estudiantes = listOf(
                    Estudiante(1,"Willy Bai Zai","willy3@gmail.com",25),
                    Estudiante(2,"Emidalia Almarante","emiEmi@gmail.com",22),
                    Estudiante(3,"Eustácio Coragem","lupussRoze@hotmail.com",19)
                )
            ),
            onDrawer = {},
            onEvent = {},
            onNavigateToCreate = {},
            onNavigateToEdit = {}
        )
    }
}

@Preview(showBackground = true, name = "Lista Vacía")
@Composable
private fun EstudianteListEmptyPreview() {
    MaterialTheme {
        EstudianteListBody(
            state = EstudianteListUiState(isLoading = false, estudiantes = emptyList()),
            onDrawer = {},
            onEvent = {},
            onNavigateToCreate = {},
            onNavigateToEdit = {}
        )
    }
}