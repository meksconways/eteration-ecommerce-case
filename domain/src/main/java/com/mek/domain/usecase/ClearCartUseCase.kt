package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val repo: CartRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<Unit, Unit>(dispatcher) {
    override suspend fun getExecutable(params: Unit) = repo.clearCart()
}