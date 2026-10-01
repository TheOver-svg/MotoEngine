package diagnostic.motoengine.kpz.data.remote.dto

data class DiagnosisRequestDto(
    val selectedSymptomCodes: List<String>
)

data class FiredRuleDto(
    val ruleCode: String,
    val conclusionCode: String,
    val conclusionText: String,
    val because: List<String>? = null
)

data class DiagnosisResultDto(
    val firedRules: List<FiredRuleDto>
)