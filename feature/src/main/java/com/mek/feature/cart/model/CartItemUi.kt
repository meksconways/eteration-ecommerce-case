package com.mek.feature.cart.model

import com.mek.domain.model.CartItem

data class CartItemUi(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val priceText: String,
    val quantity: Int,
    val lineTotalText: String
)

fun CartItem.toUi(): CartItemUi {
    val lineTotal = price * quantity
    return CartItemUi(
        id = productId,
        name = name,
        imageUrl = imageUrl,
        priceText = "%,.0f ₺".format(price),
        quantity = quantity,
        lineTotalText = "%,.0f ₺".format(lineTotal)
    )
}