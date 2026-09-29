package diagnostic.motoengine.kpz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import diagnostic.motoengine.kpz.ui.navigation.NavGraph
import diagnostic.motoengine.kpz.ui.theme.MotoEngineTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotoEngineTheme {
                NavGraph()
            }
        }
    }
}