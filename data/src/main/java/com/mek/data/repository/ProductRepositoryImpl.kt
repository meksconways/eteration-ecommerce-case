package com.mek.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mek.data.datasource.local.LocalDataSource
import com.mek.data.datasource.remote.RemoteDataSource
import com.mek.data.network.dto.toEntity
import com.mek.data.persistent.entity.toDomain
import com.mek.domain.model.Product
import com.mek.domain.repository.ProductRepository
import com.mek.domain.repository.Sort
import com.mek.domain.util.DataState
import com.mek.domain.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : ProductRepository {

    override suspend fun refreshCatalog(): DataState<Unit> {
        return when (val res = remote.fetchProducts()) {
            is NetworkResult.Success -> {
                try {
                    val entities = res.body.map { it.toEntity() }
                    local.clearProducts()
                    local.upsertProducts(entities)
                    DataState.Success(Unit)
                } catch (e: Exception) {
                    DataState.Failure(e.message ?: "Failed to cache products.")
                }
            }

            is NetworkResult.Failure -> {
                DataState.Failure(res.errorMessage)
            }
        }
    }

    override fun pagedProducts(
        query: String?,
        brands: List<String>?,
        models: List<String>?,
        minPrice: Double?,
        maxPrice: Double?,
        sort: Sort
    ): Flow<PagingData<Product>> {
        val sortKey = sort.name
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
        ) {
            local.pagingProducts(
                query = query?.trim()?.takeIf { it.isNotEmpty() },
                brands = brands?.takeIf { it.isNotEmpty() },
                models = models?.takeIf { it.isNotEmpty() },
                minPrice = minPrice,
                maxPrice = maxPrice,
                sort = sortKey
            )
        }.flow.map { paging ->
            paging.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: String): DataState<Product> {
        return try {
            val entity = local.getProductById(id)
            if (entity == null) {
                DataState.Failure("Product not found.")
            } else {
                DataState.Success(entity.toDomain())
            }
        } catch (e: Exception) {
            DataState.Failure(e.message ?: "Failed to load product.")
        }
    }

    override suspend fun getAllBrands() = local.getBrands()
    override suspend fun getAllModels() = local.getModels()

    companion object {
        private const val PAGE_SIZE = 4
    }
}