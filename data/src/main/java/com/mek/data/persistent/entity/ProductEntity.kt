package com.mek.data.persistent.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mek.domain.model.Product

@Entity(
    tableName = "products",
    indices = [
        Index("brand"),
        Index("model"),
        Index("price"),
        Index("createdAt")
    ]
)
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val image: String?,
    val description: String?,
    val brand: String?,
    val model: String?,
    val price: Double,
    val createdAt: Long
)

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    name = name,
    image = image,
    description = description,
    brand = brand,
    model = model,
    price = price,
    createdAt = createdAt
)

