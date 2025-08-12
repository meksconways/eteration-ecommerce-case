package com.mek.data.persistent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mek.data.persistent.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FavoriteEntity): Long

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun delete(productId: String)

    @Query("SELECT productId FROM favorites")
    fun observeAllIds(): Flow<List<String>>
}