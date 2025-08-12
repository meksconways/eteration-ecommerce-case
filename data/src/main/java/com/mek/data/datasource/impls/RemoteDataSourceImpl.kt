package com.mek.data.datasource.impls

import com.mek.data.datasource.remote.RemoteDataSource
import com.mek.data.network.service.ProductApiService
import com.mek.data.network.util.safeApiCall
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val productApiService: ProductApiService,
) : RemoteDataSource {
    override suspend fun fetchProducts() = safeApiCall { productApiService.getProducts() }

    override suspend fun fetchProduct(id: String) =
        safeApiCall { productApiService.getProductById(id) }
}