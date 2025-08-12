package com.mek.domain.model

data class Product(
    val id: String,
    val name: String,
    val image: String?,
    val price: Double,
    val createdAt: Long?,
    val description: String?,
    val brand: String?,
    val model: String?
)