package com.mek.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseUseCase<in Params, Type>(
    private val dispatcher: CoroutineDispatcher
) {
    protected abstract suspend fun getExecutable(params: Params): Type

    suspend operator fun invoke(params: Params): Type {
        return withContext(dispatcher) {
            getExecutable(params)
        }
    }
}