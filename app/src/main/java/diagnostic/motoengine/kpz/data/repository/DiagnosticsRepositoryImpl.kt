package diagnostic.motoengine.kpz.data.repository


import diagnostic.motoengine.kpz.data.remote.api.DiagnosticsApi
import diagnostic.motoengine.kpz.data.remote.dto.DiagnosisRequestDto
import diagnostic.motoengine.kpz.data.remote.dto.RuleDto
import diagnostic.motoengine.kpz.data.remote.dto.SymptomDto
import diagnostic.motoengine.kpz.domain.model.FiredRule
import diagnostic.motoengine.kpz.domain.model.Rule
import diagnostic.motoengine.kpz.domain.model.Symptom
import javax.inject.Inject

class DiagnosticsRepositoryImpl @Inject constructor(
    private val api: DiagnosticsApi
) : DiagnosticsRepository {

    override suspend fun getSymptoms(): List<Symptom> =
        api.getSymptoms().map { Symptom(it.code, it.label, it.group) }

    override suspend fun getRules(): List<Rule> =
        api.getRules().map {
            Rule(
                it.ruleCode,
                it.conditions,
                it.conclusionCode,
                it.conclusionText
            )
        }

    override suspend fun createSymptom(code: String, label: String, group: String) {
        api.createSymptom(SymptomDto(id = null, code = code, label = label, group = group))
    }

    override suspend fun createRule(
        ruleCode: String,
        conditions: List<String>,
        conclusionCode: String,
        conclusionText: String
    ) {
        api.createRule(
            RuleDto(
                id = null,
                ruleCode = ruleCode,
                conditions = conditions,
                conclusionCode = conclusionCode,
                conclusionText = conclusionText
            )
        )
    }

    override suspend fun diagnose(selectedCodes: List<String>): List<FiredRule> =
        api.diagnose(DiagnosisRequestDto(selectedCodes)).firedRules.map {
            FiredRule(it.ruleCode, it.conclusionCode, it.conclusionText)
        }
}