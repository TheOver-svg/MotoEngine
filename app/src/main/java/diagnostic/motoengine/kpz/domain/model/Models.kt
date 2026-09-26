package diagnostic.motoengine.kpz.domain.model

data class Symptom(
    val code: String,
    val label: String,
    val group: String
)

data class FiredRule(
    val ruleCode: String,
    val conclusionCode: String,
    val conclusionText: String
)