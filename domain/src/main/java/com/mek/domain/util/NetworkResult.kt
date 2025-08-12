package com.mek.domain.util

sealed class NetworkResult<out T> {
    data class Success<T>(val body: T) : NetworkResult<T>()
    data class Failure(val errorMessage: String) : NetworkResult<Nothing>()
}

suspend fun <T> NetworkResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Success) {
        executable(this.body)
    }
}

fun <T> NetworkResult<T>.getOrNull(): T? {
    return when (this) {
        is NetworkResult.Success -> this.body
        is NetworkResult.Failure -> null
    }
}

suspend fun <T> NetworkResult<T>.onFailure(
    executable: suspend (String) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Failure) {
        executable(errorMessage)
    }
}

fun <D, T> NetworkResult<D>.mapToDataState(
    dataTransform: (D) -> T
): DataState<T> = when (this) {
    is NetworkResult.Success -> DataState.Success(dataTransform(body))
    is NetworkResult.Failure -> DataState.Failure(errorMessage)
}

fun <T> NetworkResult<T>.mapToDataState(): DataState<T> =
    this.mapToDataState { it }

fun <T> NetworkResult<T>.mapToUnitDataState(): DataState<Unit> =
    this.mapToDataState { }