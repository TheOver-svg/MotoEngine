package diagnostic.motoengine.kpz.data.remote.dto

data class RuleDto(
    val id: String?,
    val ruleCode: String,
    val conditions: List<String>,
    val conclusionCode: String,
    val conclusionText: String
)