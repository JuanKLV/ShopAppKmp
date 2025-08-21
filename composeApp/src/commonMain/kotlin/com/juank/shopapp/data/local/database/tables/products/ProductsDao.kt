package com.juank.shopapp.data.local.database.tables.products

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductsDao {

    @Query("SELECT COUNT(*) FROM Products")
    suspend fun countProducts(): Int

    @Query("SELECT * FROM Products")
    suspend fun getProducts(): List<Products>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProducts(products: List<Products>)
}