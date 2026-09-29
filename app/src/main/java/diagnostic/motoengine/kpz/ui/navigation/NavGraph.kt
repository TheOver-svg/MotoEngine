package diagnostic.motoengine.kpz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import diagnostic.motoengine.kpz.ui.screens.ResultScreen
import diagnostic.motoengine.kpz.ui.screens.SymptomsScreen
import diagnostic.motoengine.kpz.viewmodel.SymptomsViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Symptoms.route) {

        composable(Screen.Symptoms.route) { backStackEntry ->
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
    }
}