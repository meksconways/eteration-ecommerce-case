package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.repository.ProductRepository
import com.mek.domain.util.DataState
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RefreshCatalogUseCase @Inject constructor(
    private val repository: ProductRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, DataState<Unit>>(dispatcher) {

    override suspend fun getExecutable(params: Unit): DataState<Unit> {
        return repository.refreshCatalog()
    }
}