package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DecrementCartUseCase @Inject constructor(
    private val repo: CartRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<String, Unit>(dispatcher) { // productId
    override suspend fun getExecutable(params: String) = repo.dec(params)
}