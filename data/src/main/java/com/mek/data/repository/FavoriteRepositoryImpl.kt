package com.mek.data.repository

import com.mek.data.datasource.local.LocalDataSource
import com.mek.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val local: LocalDataSource
) : FavoriteRepository {

    override suspend fun toggle(productId: String) {
        val current = local.observeFavoritesIds().first()
        if (productId in current) local.removeFromFavorites(productId) else local.addFavorites(
            productId
        )
    }

    override fun observeIds(): Flow<Set<String>> = local.observeFavoritesIds()
}