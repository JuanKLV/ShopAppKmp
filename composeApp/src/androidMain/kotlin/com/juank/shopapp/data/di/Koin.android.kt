package com.juank.shopapp.data.di

import com.juank.shopapp.data.local.database.UserDatabaseFactory
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { UserDatabaseFactory(androidApplication()) }
    }