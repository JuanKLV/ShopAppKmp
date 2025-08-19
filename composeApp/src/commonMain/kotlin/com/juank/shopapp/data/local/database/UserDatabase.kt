package com.juank.shopapp.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.juank.shopapp.data.local.database.tables.products.Products
import com.juank.shopapp.data.local.database.tables.products.ProductsDao
import com.juank.shopapp.domain.utils.Constants.CURRENT_ROOM_DB_VERSION

@Database(
    entities = [
        Products::class],
    version = CURRENT_ROOM_DB_VERSION)
@ConstructedBy(UserDatabaseConstructor::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun productsDao(): ProductsDao
    companion object{
        const val DB_NAME = "user.db"
    }
}