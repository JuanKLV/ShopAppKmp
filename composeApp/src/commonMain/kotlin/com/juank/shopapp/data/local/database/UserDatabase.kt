package com.juank.shopapp.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsDao
import com.juank.shopapp.data.local.database.tables.cartItems.CartItemsTable
import com.juank.shopapp.data.local.database.tables.categories.Categories
import com.juank.shopapp.data.local.database.tables.categories.CategoriesDao
import com.juank.shopapp.data.local.database.tables.products.Products
import com.juank.shopapp.data.local.database.tables.products.ProductsDao
import com.juank.shopapp.domain.utils.Constants.CURRENT_ROOM_DB_VERSION

@Database(
    entities = [
        Products::class,
        Categories::class,
        CartItemsTable::class],
    version = CURRENT_ROOM_DB_VERSION)
@ConstructedBy(UserDatabaseConstructor::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun cartItemsDao(): CartItemsDao
    abstract fun productsDao(): ProductsDao
    abstract fun categoriesDao(): CategoriesDao
    companion object{
        const val DB_NAME = "user.db"
    }
}