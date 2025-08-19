package com.juank.shopapp.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserDatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<UserDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(UserDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}