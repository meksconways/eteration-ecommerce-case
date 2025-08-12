package com.mek.domain.usecase

import androidx.paging.PagingData
import com.mek.domain.di.IoDispatcher
import com.mek.domain.model.Product
import com.mek.domain.repository.ProductRepository
import com.mek.domain.repository.Sort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedProductsUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<GetPagedProductsUseCase.Params, Flow<PagingData<Product>>>(dispatcher) {

    override suspend fun getExecutable(params: Params): Flow<PagingData<Product>> {
        return repository.pagedProducts(
            query = params.query,
            brands = params.brands?.toList(),
            models = params.models?.toList(),
            minPrice = params.minPrice,
            maxPrice = params.maxPrice,
            sort = params.sort
        )
    }

    data class Params(
        val query: String? = null,
        val brands: Set<String>? = null,
        val models: Set<String>? = null,
        val minPrice: Double? = null,
        val maxPrice: Double? = null,
        val sort: Sort = Sort.PRICE_HIGH_TO_LOW
    )
}