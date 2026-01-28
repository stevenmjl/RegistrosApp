package edu.ucne.registrosapp.presentation.estudiante.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrosapp.presentation.components.ConfirmDeleteDialog
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EstudianteEditScreen(
    estudianteId: Int?,
    viewModel: EstudianteEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(estudianteId) {
        viewModel.onEvent(EstudianteEditUiEvent.Load(estudianteId ?: 0))
    }

    if (showDeleteDialog) {
        ConfirmDeleteDialog(
            onConfirm = {
                viewModel.onEvent(EstudianteEditUiEvent.Delete)
                onNavigateBack()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    EstudianteEditBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onDeleteClick = { showDeleteDialog = true },
        onSaveSuccess = {
            if (state.nombresError == null && state.emailError == null && state.edadError == null) {
                onNavigateBack()
            }
        }
    )
}

@Composable
private fun EstudianteEditBody(
    state: EstudianteEditUiState,
    onEvent: (EstudianteEditUiEvent) -> Unit,
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
            // Cabecera de Persona
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (state.isNew) "Nuevo Estudiante" else "Editar Estudiante",
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

            // Campo Nombres
            OutlinedTextField(
                value = state.nombres,
                onValueChange = { onEvent(EstudianteEditUiEvent.NombresChanged(it)) },
                label = { Text("Nombre completo") },
                isError = state.nombresError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.nombresError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(16.dp))

            // Campo Email
            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(EstudianteEditUiEvent.EmailChanged(it)) },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = state.emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.emailError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(16.dp))

            // Campo Edad
            OutlinedTextField(
                value = state.edad,
                onValueChange = { onEvent(EstudianteEditUiEvent.EdadChanged(it)) },
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.edadError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.edadError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(Modifier.height(32.dp))

            // Botones
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        onEvent(EstudianteEditUiEvent.Save)
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
                        Text("Guardar Estudiante")
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

@Preview(showBackground = true, name = "Nuevo Estudiante")
@Composable
private fun EstudianteAddPreview() {
    MaterialTheme {
        EstudianteEditBody(
            state = EstudianteEditUiState(isNew = true),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {},
            onSaveSuccess = {}
        )
    }
}

@Preview(showBackground = true, name = "Editar con datos")
@Composable
private fun EstudianteEditPreview() {
    MaterialTheme {
        EstudianteEditBody(
            state = EstudianteEditUiState(
                isNew = false,
                nombres = "Emidalia Almarante",
                email = "emiEmi@gmail.com",
                edad = "22"
            ),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {},
            onSaveSuccess = {}
        )
    }
}

@Preview(showBackground = true, name = "Editar con errores")
@Composable
private fun EstudianteEditErrorsPreview() {
    MaterialTheme {
        EstudianteEditBody(
            state = EstudianteEditUiState(
                isNew = false,
                nombres = "",
                nombresError = "El nombre es requerido",
                email = "correo-invalido",
                emailError = "Formato de email inválido",
                edad = "999",
                edadError = "Edad fuera de rango"
            ),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {},
            onSaveSuccess = {}
        )
    }
}