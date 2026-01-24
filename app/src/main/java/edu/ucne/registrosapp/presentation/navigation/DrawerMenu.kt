package edu.ucne.registrosapp.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.MenuBook // Nuevo ícono
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucne.registrosapp.R
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    navHostController: NavHostController,
    content: @Composable () -> Unit
) {
    val selectedItem = remember { mutableStateOf("Estudiantes") }
    val scope = rememberCoroutineScope()

    fun handleItemClick(destination: Screen, item: String) {
        navHostController.navigate(destination)
        selectedItem.value = item
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    // Item de Estudiantes
                    item {
                        DrawerItem(
                            title = stringResource(R.string.drawer_estudiantes),
                            icon = Icons.Filled.People,
                            isSelected = selectedItem.value == stringResource(R.string.drawer_estudiantes)
                        ) {
                            handleItemClick(Screen.EstudianteList, it)
                        }
                    }

                    // Item de Asignaturas
                    item {
                        DrawerItem(
                            title = stringResource(R.string.drawer_asignaturas),
                            icon = Icons.AutoMirrored.Filled.MenuBook,
                            isSelected = selectedItem.value == stringResource(R.string.drawer_asignaturas)
                        ) {
                            handleItemClick(Screen.AsignaturaList, it)
                        }
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        content()
    }
}