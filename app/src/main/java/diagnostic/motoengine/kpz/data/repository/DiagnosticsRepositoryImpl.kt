package diagnostic.motoengine.kpz.data.repository


import diagnostic.motoengine.kpz.data.remote.api.DiagnosticsApi
import diagnostic.motoengine.kpz.data.remote.dto.DiagnosisRequestDto
import diagnostic.motoengine.kpz.domain.model.FiredRule
import diagnostic.motoengine.kpz.domain.model.Symptom
import javax.inject.Inject

class DiagnosticsRepositoryImpl @Inject constructor(
    private val api: DiagnosticsApi
) : DiagnosticsRepository {

    override suspend fun getSymptoms(): List<Symptom> =
        api.getSymptoms().map { Symptom(it.code, it.label, it.group) }

    override suspend fun diagnose(selectedCodes: List<String>): List<FiredRule> =
        api.diagnose(DiagnosisRequestDto(selectedCodes)).firedRules.map {
            FiredRule(it.ruleCode, it.conclusionCode, it.conclusionText)
        }
}