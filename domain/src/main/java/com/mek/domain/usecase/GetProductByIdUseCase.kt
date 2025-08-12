package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.model.Product
import com.mek.domain.repository.ProductRepository
import com.mek.domain.util.DataState
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<String, DataState<Product>>(dispatcher) {

    override suspend fun getExecutable(params: String): DataState<Product> {
        return repository.getById(params)
    }
}