package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteIdsUseCase @Inject constructor(
    private val repo: FavoriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Set<String>>(dispatcher) {
    override fun execute(params: Unit): Flow<Set<String>> = repo.observeIds()
}