package com.mek.data.repository

import com.mek.data.datasource.local.LocalDataSource
import com.mek.data.persistent.entity.CartItemEntity
import com.mek.domain.model.CartItem
import com.mek.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : CartRepository {

    override suspend fun addFromDetail(item: CartItem) =
        localDataSource.addOrIncrement(
            CartItemEntity(
                productId = item.productId,
                name = item.name,
                price = item.price,
                imageUrl = item.imageUrl,
                quantity = 1
            ),
            delta = 1
        )

    override suspend fun inc(productId: String) = localDataSource.increment(productId)
    override suspend fun dec(productId: String) = localDataSource.decrement(productId)

    override fun observeItems(): Flow<List<CartItem>> =
        localDataSource.observeItems().map {
            it.map { e ->
                CartItem(e.productId, e.name, e.price, e.imageUrl, e.quantity)
            }
        }

    override fun observeBadge(): Flow<Int> = localDataSource.observeBadge().distinctUntilChanged()
    override suspend fun clearCart() = localDataSource.clearCart()
}