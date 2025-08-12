package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.model.CartItem
import com.mek.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repo: CartRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<CartItem, Unit>(dispatcher) {
    override suspend fun getExecutable(params: CartItem) = repo.addFromDetail(params)
}