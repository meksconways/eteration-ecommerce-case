package com.mek.data.di

import android.content.Context
import androidx.room.Room
import com.mek.data.persistent.dao.CartDao
import com.mek.data.persistent.dao.FavoriteDao
import com.mek.data.persistent.dao.ProductDao
import com.mek.data.persistent.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "eteration_case.db")
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Provides
    @Singleton
    fun providesCartDao(db: AppDatabase): CartDao {
        return db.cartDao()
    }

    @Provides
    @Singleton
    fun providesFavoritesDao(db: AppDatabase): FavoriteDao = db.favoritesDao()
}