package com.mek.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher
) {
    protected abstract fun execute(params: P): Flow<R>

    operator fun invoke(params: P): Flow<R> = execute(params).flowOn(dispatcher)
}