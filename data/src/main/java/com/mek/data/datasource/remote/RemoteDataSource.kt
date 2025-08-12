package com.mek.data.datasource.remote

import com.mek.data.network.dto.ProductDto
import com.mek.domain.util.NetworkResult

interface RemoteDataSource {
    suspend fun fetchProducts(): NetworkResult<List<ProductDto>>
    suspend fun fetchProduct(id: String): NetworkResult<ProductDto>
}