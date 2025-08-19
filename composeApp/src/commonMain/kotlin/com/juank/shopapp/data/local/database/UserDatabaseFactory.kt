package com.juank.shopapp.data.local.database

import androidx.room.RoomDatabase

expect class UserDatabaseFactory {
    fun create(): RoomDatabase.Builder<UserDatabase>
}