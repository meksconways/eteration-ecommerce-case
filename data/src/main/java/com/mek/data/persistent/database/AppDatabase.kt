package com.mek.data.persistent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mek.data.persistent.dao.CartDao
import com.mek.data.persistent.dao.FavoriteDao
import com.mek.data.persistent.dao.ProductDao
import com.mek.data.persistent.entity.CartItemEntity
import com.mek.data.persistent.entity.FavoriteEntity
import com.mek.data.persistent.entity.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        FavoriteEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun favoritesDao(): FavoriteDao
}