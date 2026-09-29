package diagnostic.motoengine.kpz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import diagnostic.motoengine.kpz.domain.model.Rule
import diagnostic.motoengine.kpz.domain.model.Symptom
import diagnostic.motoengine.kpz.viewmodel.EditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(viewModel: EditorViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var tabIndex by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Редактор бази знань") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = tabIndex) {
                Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }, text = { Text("Ознаки") })
                Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }, text = { Text("Правила") })
            }

            when (tabIndex) {
                0 -> SymptomEditorTab(state.symptoms, onAdd = viewModel::addSymptom)
                1 -> RuleEditorTab(state.rules, state.symptoms, onAdd = viewModel::addRule)
            }
        }
    }
}

@Composable
private fun SymptomEditorTab(symptoms: List<Symptom>, onAdd: (String, String, String) -> Unit) {
    var code by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Нова ознака", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = code, onValueChange = { code = it },
                        label = { Text("Код") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = label, onValueChange = { label = it },
                        label = { Text("Назва ознаки") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = group, onValueChange = { group = it },
                        label = { Text("Група") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (code.isNotBlank() && label.isNotBlank() && group.isNotBlank()) {
                                onAdd(code.trim(), label.trim(), group.trim())
                                code = ""; label = ""; group = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Додати ознаку")
                    }
                }
            }
        }

        item {
            Text("Наявні ознаки (${symptoms.size})", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
        }

        items(symptoms) { s ->
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)) {
                Card(Modifier.fillMaxWidth())
                {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(s.label, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "${s.code} · ${s.group}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RuleEditorTab(rules: List<Rule>, symptoms: List<Symptom>, onAdd: (String, List<String>, String, String) -> Unit) {
    var ruleCode by remember { mutableStateOf("") }
    var conditionsText by remember { mutableStateOf("") }
    var conclusionCode by remember { mutableStateOf("") }
    var conclusionText by remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Нове правило", fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = ruleCode, onValueChange = { ruleCode = it },
                        label = { Text("Код правила (напр. R17)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = conditionsText, onValueChange = { conditionsText = it },
                        label = { Text("Умови через кому (коди ознак)") },
                        placeholder = { Text("s_overheat, s_oil_leak") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = conclusionCode, onValueChange = { conclusionCode = it },
                        label = { Text("Код висновку (напр. c17)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = conclusionText, onValueChange = { conclusionText = it },
                        label = { Text("Текст висновку/рекомендації") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            val conditions = conditionsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            if (ruleCode.isNotBlank() && conditions.isNotEmpty() && conclusionCode.isNotBlank() && conclusionText.isNotBlank()) {
                                onAdd(ruleCode.trim(), conditions, conclusionCode.trim(), conclusionText.trim())
                                ruleCode = ""; conditionsText = ""; conclusionCode = ""; conclusionText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Додати правило")
                    }
                }
            }
        }

        item {
            Text("Наявні правила (${rules.size})", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
        }

        items(rules) { r ->
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("${r.ruleCode} → ${r.conclusionCode}", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.bodyMedium)
                    Text("ЯКЩО: ${r.conditions.joinToString(", ")}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(r.conclusionText, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}