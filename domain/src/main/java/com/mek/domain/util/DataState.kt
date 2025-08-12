package com.mek.domain.util

sealed class DataState<out T> {
    data class Success<T>(val body: T) : DataState<T>()
    data class Failure(val errorMessage: String) : DataState<Nothing>()
}

inline fun <T> DataState<T>.onSuccess(
    executable: (T) -> Unit
): DataState<T> = apply {
    if (this is DataState.Success) {
        executable(this.body)
    }
}

inline fun <T> DataState<T>.onFailure(
    executable: (String) -> Unit
): DataState<T> = apply {
    if (this is DataState.Failure) {
        executable(errorMessage)
    }
}

fun <D, T> DataState<D>.map(
    dataTransform: (D) -> T
): DataState<T> = when (this) {
    is DataState.Success -> DataState.Success(dataTransform(body))
    is DataState.Failure -> DataState.Failure(errorMessage)
}