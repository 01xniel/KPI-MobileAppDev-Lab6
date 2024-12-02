package com.example.kpi_mobileappdev_lab6.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kpi_mobileappdev_lab6.CalcSharedViewModel
import com.example.kpi_mobileappdev_lab6.screens.CalcInputScreen
import com.example.kpi_mobileappdev_lab6.screens.CalcResultScreen

@Composable
fun AppNavHost() {
    // ініціалізація навігаційного контролера
    val navController = rememberNavController()

    // визначення маршрутизатора для навігації
    NavHost(
        navController = navController,
        startDestination = CalcInputRoute,
    ) {
        // екран введення параметрів калькулятора
        composable(CalcInputRoute) {
            // ініціалізація viewmodel для роботи з вхідними даними калькулятора
            val sharedViewModel: CalcSharedViewModel = viewModel(
                remember (NavBackStackEntry) { navController.getBackStackEntry(CalcInputRoute) }
            )
            CalcInputScreen(
                sharedViewModel = sharedViewModel,
                toCalcResultScreen = { navController.navigate(CalcResultRoute) }
            )
        }
        // екран результатів калькулятора
        composable(CalcResultRoute) {
            // ініціалізація viewmodel для роботи з вхідними даними калькулятора
            val sharedViewModel: CalcSharedViewModel = viewModel(
                remember (NavBackStackEntry) { navController.getBackStackEntry(CalcInputRoute) }
            )
            CalcResultScreen(
                sharedViewModel = sharedViewModel,
                toCalcInputScreen = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
