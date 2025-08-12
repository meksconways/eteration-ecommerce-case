package com.mek.feature.productdetail.model

import com.mek.domain.model.Product

data class ProductItem(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val description: String?,
    val priceText: String,
    val price: Double,
    val isFavorite: Boolean = false
)

fun ProductItem.withFav(isFav: Boolean) = copy(isFavorite = isFav)

fun Product.toUi(): ProductItem {
    val fixedImageUrl = "https://picsum.photos/640/480?random=$id"
    return ProductItem(
        id = id,
        name = name,
        imageUrl = fixedImageUrl,
        description = description,
        priceText = "$price ₺",
        price = price
    )
}
