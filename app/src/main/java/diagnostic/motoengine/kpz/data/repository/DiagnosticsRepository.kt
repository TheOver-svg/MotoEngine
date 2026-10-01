package diagnostic.motoengine.kpz.data.repository

import diagnostic.motoengine.kpz.domain.model.Answer
import diagnostic.motoengine.kpz.domain.model.ConsultationStep
import diagnostic.motoengine.kpz.domain.model.FiredRule
import diagnostic.motoengine.kpz.domain.model.Rule
import diagnostic.motoengine.kpz.domain.model.Symptom


interface DiagnosticsRepository {
    suspend fun getSymptoms(): List<Symptom>
    suspend fun getRules(): List<Rule>
    suspend fun createSymptom(code: String, label: String, group: String)
    suspend fun createRule(ruleCode: String, conditions: List<String>, conclusionCode: String, conclusionText: String)
    suspend fun diagnose(selectedCodes: List<String>): List<FiredRule>

    suspend fun consult(group: String?, answer: List<Answer> ) : ConsultationStep
}