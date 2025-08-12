package com.mek.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun toggle(productId: String)
    fun observeIds(): Flow<Set<String>>
}