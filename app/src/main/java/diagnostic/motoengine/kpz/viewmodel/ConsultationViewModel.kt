package diagnostic.motoengine.kpz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import diagnostic.motoengine.kpz.data.repository.DiagnosticsRepository
import diagnostic.motoengine.kpz.domain.model.Answer
import diagnostic.motoengine.kpz.domain.model.ConsultationStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class Stage { Loading, ChooseGroup, Asking, Result }

data class ConsultationUiState(
    val stage: Stage = Stage.Loading,
    val groups: List<String> = emptyList(),
    val group: String? = null,
    val answers: List<Answer> = emptyList(),
    val step: ConsultationStep? = null,
    val isBusy: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ConsultationViewModel @Inject constructor(
    private val repository: DiagnosticsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConsultationUiState())
    val state: StateFlow<ConsultationUiState> = _state

    init { loadGroups() }

    fun loadGroups() {
        viewModelScope.launch {
            _state.value = ConsultationUiState(stage = Stage.Loading)
            _state.value = try {
                val groups = repository.getSymptoms().map { it.group }.distinct()
                ConsultationUiState(stage = Stage.ChooseGroup, groups = groups)
            } catch (e: Exception) {
                ConsultationUiState(stage = Stage.ChooseGroup, error = e.message)
            }
        }
    }

    fun chooseGroup(group: String) = request(group, emptyList())

    fun answer(value: Boolean?) {
        val s = _state.value
        val q = s.step?.question ?: return
        request(s.group, s.answers + Answer(q.code, value))
    }

    fun back() {
        val s = _state.value
        if (s.answers.isEmpty()) {
            _state.value = s.copy(stage = Stage.ChooseGroup, group = null, step = null)
        } else {
            request(s.group, s.answers.dropLast(1))
        }
    }

    private fun request(group: String?, answers: List<Answer>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isBusy = true, error = null)
            try {
                val step = repository.consult(group, answers)
                _state.value = _state.value.copy(
                    stage = if (step.finished) Stage.Result else Stage.Asking,
                    group = group, answers = answers, step = step, isBusy = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isBusy = false, error = e.message)
            }
        }
    }
}