package com.example.appescola

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appescola.ui.theme.AcademiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AcademiaTheme {
                val vm: AcademiaViewModel = viewModel()
                val authVm: AuthViewModel = viewModel()
                AppNavigation(vm, authVm)
            }
        }
    }
}
