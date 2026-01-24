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
import edu.ucne.registrosapp.data.local.dao.AsignaturaDao
import edu.ucne.registrosapp.data.repository.AsignaturaRepositoryImpl
import edu.ucne.registrosapp.domain.repository.AsignaturaRepository
import edu.ucne.registrosapp.domain.usecase.asignatura.*

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

    // Provides para Estudiantes

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

    // Provides para Asignaturas

    @Provides
    fun provideAsignaturaDao(db: RegistrosDb): AsignaturaDao {
        return db.asignaturaDao()
    }

    @Provides
    @Singleton
    fun provideAsignaturaRepository(asignaturaDao: AsignaturaDao): AsignaturaRepository {
        return AsignaturaRepositoryImpl(asignaturaDao)
    }

    @Provides
    fun provideUpsertAsignaturaUseCase(repository: AsignaturaRepository): UpsertAsignaturaUseCase {
        return UpsertAsignaturaUseCase(repository)
    }

    @Provides
    fun provideObservarAsignaturasUseCase(repository: AsignaturaRepository): ObserveAsignaturasUseCase {
        return ObserveAsignaturasUseCase(repository)
    }

    @Provides
    fun provideGetAsignaturaUseCase(repository: AsignaturaRepository): GetAsignaturaUseCase {
        return GetAsignaturaUseCase(repository)
    }

    @Provides
    fun provideDeleteAsignaturaUseCase(repository: AsignaturaRepository): DeleteAsignaturaUseCase {
        return DeleteAsignaturaUseCase(repository)
    }

    @Provides
    fun provideGetAsignaturaByNombreUseCase(repository: AsignaturaRepository): GetAsignaturaByNombreUseCase {
        return GetAsignaturaByNombreUseCase(repository)
    }
}