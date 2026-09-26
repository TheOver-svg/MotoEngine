package diagnostic.motoengine.kpz.data.repository

import diagnostic.motoengine.kpz.domain.model.FiredRule
import diagnostic.motoengine.kpz.domain.model.Symptom


interface DiagnosticsRepository {
    suspend fun getSymptoms(): List<Symptom>
    suspend fun diagnose(selectedCodes: List<String>): List<FiredRule>
}