package edu.ucne.registrosapp.presentation.asignatura.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrosapp.domain.models.Asignatura
import edu.ucne.registrosapp.presentation.components.ConfirmDeleteDialog
import edu.ucne.registrosapp.ui.theme.RegistrosAppTheme

@Composable
fun AsignaturaListScreen(
    viewModel: AsignaturaListViewModel = hiltViewModel(),
    onDrawer: () -> Unit,
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var asignaturaIdToDelete by remember { mutableIntStateOf(0) }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.onEvent(AsignaturaListEvent.Delete(asignaturaIdToDelete))
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

    AsignaturaListBody(
        state = state,
        onDrawer = onDrawer,
        onEvent = { event ->
            if (event is AsignaturaListEvent.Delete) {
                asignaturaIdToDelete = event.id
                showDeleteDialog = true
            } else {
                viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AsignaturaListBody(
    state: AsignaturaListUiState,
    onDrawer: () -> Unit,
    onEvent: (AsignaturaListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Asignaturas") },
                navigationIcon = {
                    IconButton(onClick = onDrawer) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(AsignaturaListEvent.CreateNew) },
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

            if (state.asignaturas.isEmpty() && !state.isLoading) {
                Text(
                    "No hay asignaturas registradas.",
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
                items(state.asignaturas) { asignatura ->
                    AsignaturaCard(
                        asignatura = asignatura,
                        onClick = { onEvent(AsignaturaListEvent.Edit(asignatura.asignaturaId ?: 0)) },
                        onDelete = { onEvent(AsignaturaListEvent.Delete(asignatura.asignaturaId ?: 0)) }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun AsignaturaCard(
    asignatura: Asignatura,
    onClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick(asignatura.asignaturaId ?: 0) },
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
                    text = "${asignatura.codigo}: ${asignatura.nombre}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aula: ${asignatura.aula}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${asignatura.creditos} créditos",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(onClick = { onClick(asignatura.asignaturaId ?: 0) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
            }

            IconButton(onClick = { onDelete(asignatura.asignaturaId ?: 0) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview(showBackground = true, name = "Lista con Asignaturas")
@Composable
private fun AsignaturaListWithDataPreview() {
    RegistrosAppTheme {
        AsignaturaListBody(
            state = AsignaturaListUiState(
                isLoading = false,
                asignaturas = listOf(
                    Asignatura(
                        1,
                        "ISC-606",
                        "Programación III",
                        "Laboratorio A",
                        4
                    ),
                    Asignatura(
                        2,
                        "MAT-330",
                        "Cálculo Vectorial",
                        "Edificio A-204",
                        4
                    ),
                    Asignatura(
                        3,
                        "ADM-560",
                        "Emprendimiento",
                        "Aula Virtual",
                        2
                    )
                )
            ),
            onDrawer = {},
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Lista Vacía")
@Composable
private fun AsignaturaListEmptyPreview() {
    RegistrosAppTheme {
        AsignaturaListBody(
            state = AsignaturaListUiState(isLoading = false, asignaturas = emptyList()),
            onDrawer = {},
            onEvent = {}
        )
    }
}