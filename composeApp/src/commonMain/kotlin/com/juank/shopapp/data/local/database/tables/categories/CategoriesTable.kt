package com.juank.shopapp.data.local.database.tables.categories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class Categories(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    val categoryName: String = ""
)
