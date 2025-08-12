package com.mek.data.di

import com.mek.data.datasource.impls.LocalDataSourceImpl
import com.mek.data.datasource.impls.RemoteDataSourceImpl
import com.mek.data.datasource.local.LocalDataSource
import com.mek.data.datasource.remote.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        impl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        impl: RemoteDataSourceImpl
    ): RemoteDataSource
}