package diagnostic.motoengine.kpz.domain.model

data class Answer(val code: String, val value: Boolean?)

data class Hypothesis(val ruleCode: String, val text: String, val matched: Int, val total: Int)

data class ConsultationStep(
    val finished: Boolean,
    val question: Symptom?,
    val hypotheses: List<Hypothesis>,
    val firedRules: List<FiredRule>,
    val questionNumber: Int
)