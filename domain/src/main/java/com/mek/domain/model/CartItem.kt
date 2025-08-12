package com.mek.domain.model

data class CartItem(
    val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val quantity: Int
)
