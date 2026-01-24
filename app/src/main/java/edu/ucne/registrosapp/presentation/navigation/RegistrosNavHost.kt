package edu.ucne.registrosapp.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrosapp.presentation.estudiante.list.EstudianteListScreen
import edu.ucne.registrosapp.presentation.estudiante.edit.EstudianteEditScreen
import edu.ucne.registrosapp.presentation.asignatura.list.AsignaturaListScreen // Importar nuevas pantallas
import edu.ucne.registrosapp.presentation.asignatura.edit.AsignaturaEditScreen
import kotlinx.coroutines.launch

@Composable
fun RegistrosNavHost(
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    DrawerMenu(
        drawerState = drawerState,
        navHostController = navHostController
    ) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.EstudianteList
        ) {
            // --- FLUJO ESTUDIANTES ---
            composable<Screen.EstudianteList> {
                EstudianteListScreen(
                    onNavigateToCreate = {
                        navHostController.navigate(Screen.Estudiante(0))
                    },
                    onNavigateToEdit = { id ->
                        navHostController.navigate(Screen.Estudiante(id))
                    },
                    onDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            composable<Screen.Estudiante> {
                val args = it.toRoute<Screen.Estudiante>()
                EstudianteEditScreen(
                    estudianteId = args.estudianteId,
                    onNavigateBack = {
                        navHostController.navigateUp()
                    }
                )
            }

            // --- FLUJO ASIGNATURAS ---
            composable<Screen.AsignaturaList> {
                AsignaturaListScreen(
                    onNavigateToCreate = {
                        navHostController.navigate(Screen.Asignatura(0))
                    },
                    onNavigateToEdit = { id ->
                        navHostController.navigate(Screen.Asignatura(id))
                    },
                    onDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }

            composable<Screen.Asignatura> {
                val args = it.toRoute<Screen.Asignatura>()
                AsignaturaEditScreen(
                    asignaturaId = args.asignaturaId,
                    onNavigateBack = {
                        navHostController.navigateUp()
                    }
                )
            }
        }
    }
}