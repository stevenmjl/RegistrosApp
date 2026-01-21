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
            // Pantalla de la Lista
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
        }
    }
}