package com.mek.data.persistent.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mek.data.persistent.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("""
    SELECT * FROM products
    WHERE (:q IS NULL OR LOWER(name) LIKE '%' || LOWER(:q) || '%')
      AND (:brandsSize = 0 OR brand IN (:brands))
      AND (:modelsSize = 0 OR model IN (:models))
      AND (:minPrice IS NULL OR price >= :minPrice)
      AND (:maxPrice IS NULL OR price <= :maxPrice)
    ORDER BY
        CASE WHEN :sort = 'OLD_TO_NEW' THEN createdAt END ASC,
        CASE WHEN :sort = 'NEW_TO_OLD' THEN createdAt END DESC,
        CASE WHEN :sort = 'PRICE_HIGH_TO_LOW' THEN price END DESC,
        CASE WHEN :sort = 'PRICE_LOW_TO_HIGH' THEN price END ASC,
        name ASC
""")
    fun paging(
        q: String?,
        brands: List<String>,
        brandsSize: Int,
        models: List<String>,
        modelsSize: Int,
        minPrice: Double?,
        maxPrice: Double?,
        sort: String
    ): PagingSource<Int, ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clear()

    @Query("SELECT * FROM products WHERE id=:id LIMIT 1")
    suspend fun byId(id: String): ProductEntity?

    @Query("SELECT DISTINCT brand FROM products WHERE brand IS NOT NULL AND brand <> '' ORDER BY brand")
    suspend fun distinctBrands(): List<String>

    @Query("SELECT DISTINCT model FROM products WHERE model IS NOT NULL AND model <> '' ORDER BY model")
    suspend fun distinctModels(): List<String>
}