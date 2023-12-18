package com.fleetio.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fleetio.presentation.home.HomeScreen
import com.fleetio.presentation.detail.VehicleInfoScreen

@Preview
@Composable
fun AppNavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.FleetViewScreen.route
    ) {
        composable(
            route = Screen.FleetViewScreen.route
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.VehicleInfolDisplay.route + "/{id}"
        ) {
            VehicleInfoScreen(navController)
        }
    }
}