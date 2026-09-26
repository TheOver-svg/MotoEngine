package diagnostic.motoengine.kpz.data.remote.api


import diagnostic.motoengine.kpz.data.remote.dto.*
import retrofit2.http.*

interface DiagnosticsApi {

    @GET("api/symptoms")
    suspend fun getSymptoms(): List<SymptomDto>

    @GET("api/rules")
    suspend fun getRules(): List<RuleDto>

    @POST("api/diagnosis")
    suspend fun diagnose(@Body request: DiagnosisRequestDto): DiagnosisResultDto
}