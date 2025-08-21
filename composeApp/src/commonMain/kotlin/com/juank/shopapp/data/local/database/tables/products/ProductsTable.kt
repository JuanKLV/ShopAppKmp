package com.juank.shopapp.data.local.database.tables.products

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Products")
data class Products(
    @PrimaryKey(autoGenerate = true) val productId: Int = 0,
    val productName: String = "",
    val price: Double = 0.0,
    val image: String = "",
    val categoryId: Int = 0
)