package diagnostic.motoengine.kpz.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import diagnostic.motoengine.kpz.ui.screens.ConsultationScreen
import diagnostic.motoengine.kpz.ui.screens.EditorScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavHost(
        navController = navController,
        startDestination = Screen.Symptoms.route,
        modifier = Modifier.padding(20.dp)
    ) {
        composable(Screen.Symptoms.route) { ConsultationScreen() }
        composable(Screen.Editor.route) { EditorScreen() }
    }
}