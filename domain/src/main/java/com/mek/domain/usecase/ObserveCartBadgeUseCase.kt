package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ObserveCartBadgeUseCase @Inject constructor(
    private val repo: CartRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Int>(dispatcher) {
    override fun execute(params: Unit) = repo.observeBadge()
}