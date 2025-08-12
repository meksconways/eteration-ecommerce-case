package com.mek.data.network.util

import com.google.gson.Gson
import com.mek.domain.util.NetworkResult
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

data class ErrorObj(val message: String?)

suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> T
): NetworkResult<T> = try {
    NetworkResult.Success(apiCall())
} catch (ce: CancellationException) {
    throw ce
} catch (e: HttpException) {
    val raw = e.response()?.errorBody()?.string()
    val msg = try {
        // { "message": "Not found" }
        Gson().fromJson(raw, ErrorObj::class.java)?.message
    } catch (_: Exception) {
        null
    }
    val fallback = raw?.trim()?.removeSurrounding("\"")
    NetworkResult.Failure(msg ?: fallback ?: "Server error: ${e.code()}")
} catch (_: IOException) {
    NetworkResult.Failure("Please check your internet connection.")
} catch (e: Exception) {
    NetworkResult.Failure(e.localizedMessage ?: "Unknown error")
}