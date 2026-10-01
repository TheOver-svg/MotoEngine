package diagnostic.motoengine.kpz.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import diagnostic.motoengine.kpz.domain.model.FiredRule
import diagnostic.motoengine.kpz.domain.model.Hypothesis
import diagnostic.motoengine.kpz.viewmodel.ConsultationUiState
import diagnostic.motoengine.kpz.viewmodel.ConsultationViewModel
import diagnostic.motoengine.kpz.viewmodel.Stage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultationScreen(viewModel: ConsultationViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Експерт-діагност", fontWeight = FontWeight.SemiBold) }) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (state.stage) {
                Stage.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                Stage.ChooseGroup -> GroupChooser(state, viewModel::chooseGroup, viewModel::loadGroups)
                Stage.Asking -> AskingContent(state, viewModel)
                Stage.Result -> ResultContent(state, viewModel::back, viewModel::loadGroups)
            }
            if (state.isBusy) {
                LinearProgressIndicator(Modifier.fillMaxWidth().align(Alignment.TopCenter))
            }
        }
    }
}

private fun groupEmoji(name: String) = when {
    name.startsWith("Пуск") -> "🔑"
    name.startsWith("Робота") -> "⚙️"
    name.startsWith("Ходова") -> "🛞"
    else -> "🔧"
}

@Composable
private fun GroupChooser(state: ConsultationUiState, onPick: (String) -> Unit, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Що турбує ваш мотоцикл?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            "Оберіть сферу, і я поставлю кілька уточнюючих питань.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        state.error?.let {
            Text("Помилка: $it", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            OutlinedButton(onClick = onRetry) { Text("Спробувати ще раз") }
        }
        state.groups.forEach { group ->
            Card(
                onClick = { onPick(group) },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(groupEmoji(group), style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.width(16.dp))
                    Text(group, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
private fun AskingContent(state: ConsultationUiState, vm: ConsultationViewModel) {
    val step = state.step ?: return
    val question = step.question ?: return

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            "${state.group} · питання ${step.questionNumber}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))

        AnimatedContent(
            targetState = question,
            transitionSpec = {
                (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
            },
            label = "question"
        ) { q ->
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp)
            ) {
                Column(Modifier.padding(28.dp), verticalArrangement = Arrangement.Center) {
                    Text(
                        "Це про ваш мотоцикл?",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        q.label,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        if (step.hypotheses.isNotEmpty()) {
            HypothesesBlock(step.hypotheses)
            Spacer(Modifier.height(16.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { vm.answer(true) },
                enabled = !state.isBusy,
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Так") }
            FilledTonalButton(
                onClick = { vm.answer(false) },
                enabled = !state.isBusy,
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Ні") }
            OutlinedButton(
                onClick = { vm.answer(null) },
                enabled = !state.isBusy,
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Не знаю") }
        }
        TextButton(onClick = vm::back, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("← Назад")
        }
    }
}

@Composable
private fun HypothesesBlock(hypotheses: List<Hypothesis>) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Поточні гіпотези", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            hypotheses.forEach { h ->
                Column {
                    Text(h.text, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                    LinearProgressIndicator(
                        progress = { h.matched.toFloat() / h.total },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultContent(state: ConsultationUiState, onBack: () -> Unit, onRestart: () -> Unit) {
    val fired = state.step?.firedRules.orEmpty()

    LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                if (fired.isEmpty()) "Діагноз не встановлено" else "Ось що я думаю",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            if (fired.isEmpty()) {
                Text(
                    "За цими відповідями жодне правило не спрацювало. Спробуйте іншу сферу або додайте правило в редакторі.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
        items(fired) { ResultCard(it) }
        item {
            Button(onClick = onRestart, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
                Text("Почати заново")
            }
            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("← Змінити останню відповідь") }
        }
    }
}

@Composable
private fun ResultCard(rule: FiredRule) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(rule.ruleCode, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(rule.conclusionText, style = MaterialTheme.typography.bodyLarge)
            if (rule.because.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text(
                    "Чому: " + rule.because.joinToString(" + "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                )
            }
        }
    }
}