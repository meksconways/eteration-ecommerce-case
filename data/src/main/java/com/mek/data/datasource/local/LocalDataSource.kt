package com.mek.data.datasource.local

import androidx.paging.PagingSource
import com.mek.data.persistent.entity.CartItemEntity
import com.mek.data.persistent.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun clearProducts()
    suspend fun upsertProducts(items: List<ProductEntity>)
    suspend fun getProductById(id: String): ProductEntity?
    fun pagingProducts(
        query: String?,
        brands: List<String>?,
        models: List<String>?,
        minPrice: Double?,
        maxPrice: Double?,
        sort: String
    ): PagingSource<Int, ProductEntity>
    suspend fun getBrands(): List<String>
    suspend fun getModels(): List<String>

    //Cart stuffs
    suspend fun addOrIncrement(item: CartItemEntity, delta: Int = 1)
    suspend fun increment(productId: String)
    suspend fun decrement(productId: String)
    fun observeItems(): Flow<List<CartItemEntity>>
    fun observeBadge(): Flow<Int>
    suspend fun clearCart()

    //Favorites stuffs
    suspend fun addFavorites(productId: String)
    suspend fun removeFromFavorites(productId: String)
    fun observeFavoritesIds(): Flow<Set<String>>

}