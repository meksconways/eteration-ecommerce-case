package com.mek.data.persistent.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mek.domain.model.CartItem

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val quantity: Int
)

fun CartItemEntity.toCartItem() = CartItem(
    productId = productId,
    name = name,
    price = price,
    imageUrl = imageUrl,
    quantity = quantity
)