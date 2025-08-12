package com.mek.domain.usecase

import com.mek.domain.di.IoDispatcher
import com.mek.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repo: FavoriteRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : BaseUseCase<String, Unit>(dispatcher) {
    override suspend fun getExecutable(params: String) = repo.toggle(params)
}