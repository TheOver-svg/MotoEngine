package diagnostic.motoengine.kpz.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Symptoms : Screen("symptoms", "Діагностика", Icons.Filled.Build)
    object Editor : Screen("editor", "Редактор", Icons.Filled.Edit)
}