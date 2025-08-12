package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetFilterMetaUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, GetFilterMetaUseCase.Params>(dispatcher) {

    override suspend fun getExecutable(params: Unit): Params {
        val brands = productRepository.getAllBrands()
        val models = productRepository.getAllModels()
        return Params(brands, models)
    }

    data class Params(val brands: List<String>, val models: List<String>)
}