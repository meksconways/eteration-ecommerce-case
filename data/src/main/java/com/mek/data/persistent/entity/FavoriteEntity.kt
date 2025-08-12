package com.mek.data.persistent.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: String,
    val createdAt: Long = System.currentTimeMillis()
)