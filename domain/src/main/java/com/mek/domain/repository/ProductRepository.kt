package com.mek.domain.repository

import androidx.paging.PagingData
import com.mek.domain.model.Product
import com.mek.domain.util.DataState
import kotlinx.coroutines.flow.Flow

enum class Sort {
    OLD_TO_NEW,
    NEW_TO_OLD,
    PRICE_HIGH_TO_LOW,
    PRICE_LOW_TO_HIGH
}

interface ProductRepository {
    suspend fun refreshCatalog(): DataState<Unit>
    fun pagedProducts(
        query: String?,
        brands: List<String>?,
        models: List<String>?,
        minPrice: Double?,
        maxPrice: Double?,
        sort: Sort
    ): Flow<PagingData<Product>>
    suspend fun getById(id: String): DataState<Product>
    suspend fun getAllBrands(): List<String>
    suspend fun getAllModels(): List<String>

}
