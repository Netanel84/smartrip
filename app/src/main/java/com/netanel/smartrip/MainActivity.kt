package com.netanel.smartrip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.netanel.smartrip.ui.PlanScreen
import com.netanel.smartrip.ui.RouteSetupScreen
import com.netanel.smartrip.model.TripPlan

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "setup") {
            composable("setup") {
                RouteSetupScreen { plan ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("plan", plan)
                    navController.navigate("plan")
                }
            }
            composable("plan") {
                val plan = navController.previousBackStackEntry?.savedStateHandle?.get<TripPlan>("plan")
                if (plan != null) {
                    PlanScreen(plan)
                }
            }
        }
    }
}
