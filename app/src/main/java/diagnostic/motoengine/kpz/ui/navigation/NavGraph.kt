package diagnostic.motoengine.kpz.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import diagnostic.motoengine.kpz.ui.screens.EditorScreen
import diagnostic.motoengine.kpz.ui.screens.ResultScreen
import diagnostic.motoengine.kpz.ui.screens.SymptomsScreen
import diagnostic.motoengine.kpz.viewmodel.SymptomsViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomBarScreens.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Symptoms.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Screen.Symptoms.route) {
                val viewModel: SymptomsViewModel = hiltViewModel()
                SymptomsScreen(
                    viewModel = viewModel,
                    onShowResults = { navController.navigate(Screen.Results.route) }
                )
            }

            composable(Screen.Results.route) {
                val parentEntry = remember(navController.currentBackStackEntry) {
                    navController.getBackStackEntry(Screen.Symptoms.route)
                }
                val viewModel: SymptomsViewModel = hiltViewModel(parentEntry)
                ResultScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Editor.route) {
                EditorScreen()
            }
        }
    }
}