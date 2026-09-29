package diagnostic.motoengine.kpz.ui.navigation

sealed class Screen(val route: String) {
    object Symptoms : Screen("symptoms")
    object Results : Screen("results")
}