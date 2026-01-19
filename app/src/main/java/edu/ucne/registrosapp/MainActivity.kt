package edu.ucne.registrosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.registrosapp.presentation.navigation.RegistrosNavHost
import edu.ucne.registrosapp.ui.theme.RegistrosAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistrosAppTheme {
                val navHostController = rememberNavController()
                RegistrosNavHost(navHostController)
            }
        }
    }
}