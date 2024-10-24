package com.example.mazadyapp.di

import com.example.mazadyapp.core.data.module.NetworkModule
import com.example.mazadyapp.data.remote.api.MazadyApiService
import com.example.mazadyapp.domain.repository.MazadyRepository
import com.example.mazadyapp.data.repository.MazadyRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMazadyApi(retrofit: Retrofit): MazadyApiService {
        return retrofit.create(MazadyApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideWeatherRepository(
        homeService: MazadyApiService,
    ): MazadyRepository {
        return MazadyRepositoryImp(homeService)
    }
}