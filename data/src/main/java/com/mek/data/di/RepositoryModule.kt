package com.mek.data.di

import com.mek.data.repository.CartRepositoryImpl
import com.mek.data.repository.FavoriteRepositoryImpl
import com.mek.data.repository.ProductRepositoryImpl
import com.mek.domain.repository.CartRepository
import com.mek.domain.repository.FavoriteRepository
import com.mek.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
}