package com.juank.shopapp.data.local.database.tables.categories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoriesDao {
    @Query("SELECT COUNT(*) FROM Categories")
    suspend fun countCategories(): Int

    @Query("SELECT * FROM Categories")
    suspend fun getCategories(): List<Categories>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCategories(category: List<Categories>)
}