package diagnostic.motoengine.kpz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import diagnostic.motoengine.kpz.data.repository.DiagnosticsRepository
import diagnostic.motoengine.kpz.domain.model.FiredRule
import diagnostic.motoengine.kpz.domain.model.Symptom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SymptomsUiState(
    val symptoms: List<Symptom> = emptyList(),
    val selectedCodes: Set<String> = emptySet(),
    val results: List<FiredRule> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val repository: DiagnosticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SymptomsUiState())
    val uiState: StateFlow<SymptomsUiState> = _uiState

    init {
        loadSymptoms()
    }

    fun refresh()
    {
        loadSymptoms()
    }

    private fun loadSymptoms() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val symptoms = repository.getSymptoms()
                _uiState.value = _uiState.value.copy(symptoms = symptoms, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun toggleSymptom(code: String) {
        val current = _uiState.value.selectedCodes
        val updated = if (code in current) current - code else current + code
        _uiState.value = _uiState.value.copy(selectedCodes = updated)
    }

    fun runDiagnosis() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val results = repository.diagnose(_uiState.value.selectedCodes.toList())
                _uiState.value = _uiState.value.copy(results = results, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}