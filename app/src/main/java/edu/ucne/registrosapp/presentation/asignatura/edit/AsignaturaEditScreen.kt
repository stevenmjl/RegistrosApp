package edu.ucne.registrosapp.presentation.asignatura.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrosapp.presentation.components.ConfirmDeleteDialog
import edu.ucne.registrosapp.ui.theme.RegistrosAppTheme

@Composable
fun AsignaturaEditScreen(
    asignaturaId: Int?,
    viewModel: AsignaturaEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(asignaturaId) {
        viewModel.onEvent(AsignaturaEditUiEvent.Load(asignaturaId ?: 0))
    }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.onEvent(AsignaturaEditUiEvent.Delete)
                onNavigateBack()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    AsignaturaEditBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onDeleteClick = { showDeleteDialog = true },
        onSaveSuccess = {
            if (state.codigoError == null && state.nombreError == null &&
                state.aulaError == null && state.creditosError == null) {
                onNavigateBack()
            }
        }
    )
}

@Composable
private fun AsignaturaEditBody(
    state: AsignaturaEditUiState,
    onEvent: (AsignaturaEditUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (state.isNew) "Nueva Asignatura" else "Editar Asignatura",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 24.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            // Campo Código
            OutlinedTextField(
                value = state.codigo,
                onValueChange = { onEvent(AsignaturaEditUiEvent.CodigoChanged(it)) },
                label = { Text("Código (ej: MAT-101)") },
                isError = state.codigoError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.codigoError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(16.dp))

            // Campo Nombre
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(AsignaturaEditUiEvent.NombreChanged(it)) },
                label = { Text("Nombre de la asignatura") },
                isError = state.nombreError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.nombreError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // Campo Aula
                OutlinedTextField(
                    value = state.aula,
                    onValueChange = { onEvent(AsignaturaEditUiEvent.AulaChanged(it)) },
                    label = { Text("Aula") },
                    isError = state.aulaError != null,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(16.dp))

                // Campo Créditos
                OutlinedTextField(
                    value = state.creditos,
                    onValueChange = { onEvent(AsignaturaEditUiEvent.CreditosChanged(it)) },
                    label = { Text("Créditos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = state.creditosError != null,
                    modifier = Modifier.width(100.dp)
                )
            }

            Row {
                Box(modifier = Modifier.weight(1f)) {
                    state.aulaError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) }
                }
                Spacer(Modifier.width(16.dp))
                Box(modifier = Modifier.width(100.dp)) {
                    state.creditosError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall) }
                }
            }

            Spacer(Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        onEvent(AsignaturaEditUiEvent.Save)
                        onSaveSuccess()
                    },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Guardar")
                    }
                }

                if (!state.isNew) {
                    Spacer(Modifier.width(12.dp))
                    OutlinedButton(
                        onClick = { onDeleteClick() },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

@Preview(showBackground = true, name = "Nueva Asignatura")
@Composable
private fun AsignaturaAddPreview() {
    RegistrosAppTheme {
        AsignaturaEditBody(
            state = AsignaturaEditUiState(isNew = true),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {},
            onSaveSuccess = {}
        )
    }
}

@Preview(showBackground = true, name = "Editar Asignatura")
@Composable
private fun AsignaturaEditPreview() {
    RegistrosAppTheme {
        AsignaturaEditBody(
            state = AsignaturaEditUiState(
                isNew = false,
                asignaturaId = 1,
                codigo = "ISC-606",
                nombre = "Programación III",
                aula = "Lab C",
                creditos = "4"
            ),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {},
            onSaveSuccess = {}
        )
    }
}

@Preview(showBackground = true, name = "Asignatura con Errores")
@Composable
private fun AsignaturaEditErrorsPreview() {
    RegistrosAppTheme {
        AsignaturaEditBody(
            state = AsignaturaEditUiState(
                isNew = false,
                codigo = "",
                codigoError = "El código es requerido",
                nombre = "Prog",
                nombreError = "Nombre demasiado corto",
                aula = "",
                aulaError = "El aula es requerida",
                creditos = "0",
                creditosError = "Debe ser mayor a 0"
            ),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {},
            onSaveSuccess = {}
        )
    }
}