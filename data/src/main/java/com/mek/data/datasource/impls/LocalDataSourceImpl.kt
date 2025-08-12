package com.mek.data.datasource.impls

import com.mek.data.datasource.local.LocalDataSource
import com.mek.data.persistent.dao.CartDao
import com.mek.data.persistent.dao.FavoriteDao
import com.mek.data.persistent.dao.ProductDao
import com.mek.data.persistent.entity.CartItemEntity
import com.mek.data.persistent.entity.FavoriteEntity
import com.mek.data.persistent.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) : LocalDataSource {

    override suspend fun clearProducts() = productDao.clear()

    override suspend fun upsertProducts(items: List<ProductEntity>) =
        productDao.upsertAll(items)

    override suspend fun getProductById(id: String) =
        productDao.byId(id)

    override fun pagingProducts(
        query: String?,
        brands: List<String>?,
        models: List<String>?,
        minPrice: Double?,
        maxPrice: Double?,
        sort: String
    ) = productDao.paging(
        q = query,
        brands = brands ?: emptyList(),
        brandsSize = brands?.size ?: 0,
        models = models ?: emptyList(),
        modelsSize = models?.size ?: 0,
        minPrice = minPrice,
        maxPrice = maxPrice,
        sort = sort
    )

    override suspend fun getBrands(): List<String> {
        return productDao.distinctBrands()
    }

    override suspend fun getModels(): List<String> {
        return productDao.distinctModels()
    }

    override suspend fun addOrIncrement(item: CartItemEntity, delta: Int) =
        cartDao.addOrIncrement(item, delta)

    override suspend fun increment(productId: String) =
        cartDao.incrementQuantity(productId, 1)

    override suspend fun decrement(productId: String) =
        cartDao.decrement(productId)

    override fun observeItems(): Flow<List<CartItemEntity>> = cartDao.observeAll()
    override fun observeBadge(): Flow<Int> = cartDao.observeTotalQuantity()
    override suspend fun clearCart() = cartDao.deleteAll()

    override suspend fun addFavorites(productId: String) {
        favoriteDao.insert(FavoriteEntity(productId))
    }

    override suspend fun removeFromFavorites(productId: String) {
        favoriteDao.delete(productId)
    }

    override fun observeFavoritesIds(): Flow<Set<String>> =
        favoriteDao.observeAllIds().map { it.toSet() }.distinctUntilChanged()
}