package diagnostic.motoengine.kpz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import diagnostic.motoengine.kpz.data.repository.DiagnosticsRepository
import diagnostic.motoengine.kpz.domain.model.Rule
import diagnostic.motoengine.kpz.domain.model.Symptom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditorUiState(
    val symptoms: List<Symptom> = emptyList(),
    val rules: List<Rule> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: DiagnosticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val symptoms = repository.getSymptoms()
                val rules = repository.getRules()
                _uiState.value = _uiState.value.copy(symptoms = symptoms, rules = rules, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, message = "Помилка завантаження: ${e.message}")
            }
        }
    }

    fun addSymptom(code: String, label: String, group: String) {
        viewModelScope.launch {
            try {
                repository.createSymptom(code, label, group)
                _uiState.value = _uiState.value.copy(message = "Ознаку додано")
                refresh()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(message = "Помилка: ${e.message}")
            }
        }
    }

    fun addRule(ruleCode: String, conditions: List<String>, conclusionCode: String, conclusionText: String) {
        viewModelScope.launch {
            try {
                repository.createRule(ruleCode, conditions, conclusionCode, conclusionText)
                _uiState.value = _uiState.value.copy(message = "Правило додано")
                refresh()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(message = "Помилка: ${e.message}")
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}