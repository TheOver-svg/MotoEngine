package diagnostic.motoengine.kpz.data.remote.dto

data class AnswerDto(val code: String, val value: Boolean?)

data class ConsultationRequestDto(val group: String?, val answers: List<AnswerDto>)

data class HypothesisDto(val ruleCode: String, val text: String, val matched: Int, val total: Int)

data class ConsultationResponseDto(
    val finished: Boolean,
    val question: SymptomDto?,
    val hypotheses: List<HypothesisDto>?,
    val firedRules: List<FiredRuleDto>?,
    val questionNumber: Int
)