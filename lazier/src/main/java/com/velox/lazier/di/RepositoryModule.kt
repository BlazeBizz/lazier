package com.velox.lazier.di

import com.velox.lazier.data.remote.ApiService
import com.velox.lazier.data.repository.RepositoryImp
import com.velox.lazier.domain.repositories.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        apiService: ApiService
    ): Repository {
        return RepositoryImp(
            apiService = apiService
        )
    }
}

//crVelox