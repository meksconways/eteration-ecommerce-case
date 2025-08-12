package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.model.CartItem
import com.mek.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ObserveCartItemsUseCase @Inject constructor(
    private val repo: CartRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<CartItem>>(dispatcher) {
    override fun execute(params: Unit) = repo.observeItems()
}