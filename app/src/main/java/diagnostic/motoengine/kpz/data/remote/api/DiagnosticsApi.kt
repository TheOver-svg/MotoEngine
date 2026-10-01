package diagnostic.motoengine.kpz.data.remote.api

import diagnostic.motoengine.kpz.data.remote.dto.*
import retrofit2.http.*

interface DiagnosticsApi {

    @GET("api/symptoms")
    suspend fun getSymptoms(): List<SymptomDto>

    @POST("api/symptoms")
    suspend fun createSymptom(@Body symptom: SymptomDto): SymptomDto

    @GET("api/rules")
    suspend fun getRules(): List<RuleDto>

    @POST("api/rules")
    suspend fun createRule(@Body rule: RuleDto): RuleDto

    @POST("api/diagnosis")
    suspend fun diagnose(@Body request: DiagnosisRequestDto): DiagnosisResultDto

    @POST("api/consultation/next")
    suspend fun consult(@Body request: ConsultationRequestDto): ConsultationResponseDto
}