package com.mek.data.network.dto

import com.mek.data.persistent.entity.ProductEntity
import java.time.Instant

data class ProductDto(
    val id: String?,
    val createdAt: String?,
    val name: String?,
    val image: String?,
    val price: String?,
    val description: String?,
    val model: String?,
    val brand: String?
)

fun ProductDto.toEntity(): ProductEntity = ProductEntity(
    id = id.orEmpty(),
    name = name.orEmpty(),
    image = image,
    description = description,
    brand = brand,
    model = model,
    price = price?.toDoubleOrNull() ?: 0.0,
    createdAt = createdAt
        ?.let { runCatching { Instant.parse(it).toEpochMilli() }.getOrNull() }
        ?: 0L
)

/**
 *
 * {
 * "createdAt": "2023-07-17T07:21:02.529Z",
 * "name": "Bentley Focus",
 * "image": "https://loremflickr.com/640/480/food",
 * "price": "51.00",
 * "description": "Quasi adipisci sint veniam delectus. Illum laboriosam minima dignissimos natus earum facere consequuntur eius vero. Itaque facilis at tempore ipsa. Accusamus nihil fugit velit possimus expedita error porro aliquid. Optio magni mollitia veritatis repudiandae tenetur nemo. Id consectetur fuga ipsam quidem voluptatibus sed magni dolore.\nFacilis commodi dolores sapiente delectus nihil ex a perferendis. Totam deserunt assumenda inventore. Incidunt nesciunt adipisci natus porro deleniti nisi incidunt laudantium soluta. Nostrum optio ab facilis quisquam.\nSoluta laudantium ipsa ut accusantium possimus rem. Illo voluptatibus culpa incidunt repudiandae placeat animi. Delectus id in animi incidunt autem. Ipsum provident beatae nisi cumque nulla iure.",
 * "model": "CTS",
 * "brand": "Lamborghini",
 * "id": "1"
 * }
 * */