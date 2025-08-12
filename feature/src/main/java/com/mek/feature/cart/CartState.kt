package com.mek.feature.cart

import com.mek.feature.cart.model.CartItemUi

data class CartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val items: List<CartItemUi> = emptyList(),
    val totalText: String = "0 ₺",
    val isEmpty: Boolean = true
)