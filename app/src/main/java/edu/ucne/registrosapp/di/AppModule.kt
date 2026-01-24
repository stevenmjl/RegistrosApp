package edu.ucne.registrosapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrosapp.data.local.dao.EstudianteDao
import edu.ucne.registrosapp.data.local.database.RegistrosDb
import edu.ucne.registrosapp.data.repository.EstudianteRepositoryImpl
import edu.ucne.registrosapp.domain.repository.EstudianteRepository
import javax.inject.Singleton
import edu.ucne.registrosapp.domain.usecase.estudiante.GetEstudianteUseCase
import edu.ucne.registrosapp.domain.usecase.estudiante.ObserveEstudiantesUseCase
import edu.ucne.registrosapp.domain.usecase.estudiante.UpsertEstudianteUseCase
import edu.ucne.registrosapp.domain.usecase.estudiante.DeleteEstudianteUseCase

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRegistrosDb(@ApplicationContext appContext: Context): RegistrosDb {
        return Room.databaseBuilder(
            appContext,
            RegistrosDb::class.java,
            "Registros.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEstudianteDao(db: RegistrosDb): EstudianteDao {
        return db.estudianteDao()
    }

    @Provides
    @Singleton
    fun provideEstudianteRepository(estudianteDao: EstudianteDao): EstudianteRepository {
        return EstudianteRepositoryImpl(estudianteDao)
    }

    // Nuevos provides para UseCases

    @Provides
    fun provideUpsertEstudianteUseCase(repository: EstudianteRepository): UpsertEstudianteUseCase {
        return UpsertEstudianteUseCase(repository)
    }

    @Provides
    fun provideObserveEstudiantesUseCase(repository: EstudianteRepository): ObserveEstudiantesUseCase {
        return ObserveEstudiantesUseCase(repository)
    }

    @Provides
    fun provideGetEstudianteUseCase(repository: EstudianteRepository): GetEstudianteUseCase {
        return GetEstudianteUseCase(repository)
    }

    @Provides
    fun provideDeleteEstudianteUseCase(repository: EstudianteRepository): DeleteEstudianteUseCase {
        return DeleteEstudianteUseCase(repository)
    }
}