package com.mek.feature.productdetail

import com.mek.feature.productdetail.model.ProductItem

data class ProductDetailState(
    val isLoading: Boolean = true,
    val product: ProductItem? = null,
    val errorMessage: String? = null,
    val inCartQty: Int = 0
)