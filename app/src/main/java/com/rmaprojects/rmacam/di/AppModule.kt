package com.rmaprojects.rmacam.di

import android.app.Application
import com.rmaprojects.rmacam.data.repository.RMACamRepository
import com.rmaprojects.rmacam.data.repository.RMACamRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRepository(application: Application): RMACamRepository {
        return RMACamRepositoryImpl(application)
    }
}