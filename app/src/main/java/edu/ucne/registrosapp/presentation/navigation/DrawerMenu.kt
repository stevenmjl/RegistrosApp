package edu.ucne.registrosapp.presentation.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.ucne.registrosapp.R
import edu.ucne.registrosapp.ui.theme.RegistrosAppTheme
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

@Preview(showBackground = true)
@Composable
fun DrawerMenuPreview() {
    RegistrosAppTheme {
        DrawerMenu(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            navHostController = rememberNavController(),
            content = {}
        )
    }
}