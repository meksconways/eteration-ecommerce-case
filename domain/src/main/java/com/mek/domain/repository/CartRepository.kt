package com.mek.domain.repository

import com.mek.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addFromDetail(item: CartItem)
    suspend fun inc(productId: String)
    suspend fun dec(productId: String)
    fun observeItems(): Flow<List<CartItem>>
    fun observeBadge(): Flow<Int>
    suspend fun clearCart()
}