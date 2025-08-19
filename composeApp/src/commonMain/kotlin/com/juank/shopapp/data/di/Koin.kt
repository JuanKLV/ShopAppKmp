package com.juank.shopapp.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.juank.shopapp.data.local.database.UserDatabase
import com.juank.shopapp.data.local.database.UserDatabaseFactory
import com.juank.shopapp.data.repositoryImpl.login.LoginRepositoryImpl
import com.juank.shopapp.domain.repository.login.LoginRepository
import com.juank.shopapp.domain.useCase.login.LoginUseCase
import com.juank.shopapp.domain.utils.AppDao
import com.juank.shopapp.presentation.screens.login.LoginViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val repositoryModule : Module = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
}

val useCaseModule : Module = module {
    factory { LoginUseCase(get()) }
}

val viewModelModule : Module = module {
    factory { LoginViewModel(get()) }
}

val appDao : Module = module {
    single<AppDao> {
        AppDao(productsDao = get())
    }
}

val dataBaseModule : Module = module {
    single {
        get<UserDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<UserDatabase>().productsDao() }
}