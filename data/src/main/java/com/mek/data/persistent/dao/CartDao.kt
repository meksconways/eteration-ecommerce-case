package com.mek.data.persistent.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mek.data.persistent.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: CartItemEntity): Long

    @Query("UPDATE cart_items SET quantity = quantity + :delta WHERE productId = :productId")
    suspend fun incrementQuantity(productId: String, delta: Int = 1)

    @Transaction
    suspend fun addOrIncrement(item: CartItemEntity, delta: Int = 1) {
        val rowId = insert(item.copy(quantity = delta))
        if (rowId == -1L) incrementQuantity(item.productId, delta)
    }

    @Query(
        """
        UPDATE cart_items
        SET quantity = quantity - 1
        WHERE productId = :productId AND quantity > 0
    """
    )
    suspend fun decrementOnce(productId: String): Int

    @Query("DELETE FROM cart_items WHERE productId = :productId AND quantity <= 0")
    suspend fun deleteIfZero(productId: String)

    @Transaction
    suspend fun decrement(productId: String) {
        val affected = decrementOnce(productId)
        if (affected > 0) deleteIfZero(productId)  // qty 1 ise 0'a düşer ve silinir
    }

    @Query("DELETE FROM cart_items")
    suspend fun deleteAll()

    @Query("SELECT * FROM cart_items")
    fun observeAll(): Flow<List<CartItemEntity>>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun observeTotalQuantity(): Flow<Int>

}