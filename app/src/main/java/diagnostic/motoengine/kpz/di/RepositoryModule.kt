package diagnostic.motoengine.kpz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import diagnostic.motoengine.kpz.data.repository.DiagnosticsRepository
import diagnostic.motoengine.kpz.data.repository.DiagnosticsRepositoryImpl
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDiagnosticsRepository(
        impl: DiagnosticsRepositoryImpl
    ): DiagnosticsRepository
}