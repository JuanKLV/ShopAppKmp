package com.juank.shopapp.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.juank.shopapp.data.local.database.UserDatabase
import com.juank.shopapp.data.local.database.UserDatabaseFactory
import com.juank.shopapp.data.remote.services.auth.AuthService
import com.juank.shopapp.data.remote.services.auth.AuthServiceImpl
import com.juank.shopapp.data.repositoryImpl.login.LoginRepositoryImpl
import com.juank.shopapp.domain.repository.login.LoginRepository
import com.juank.shopapp.domain.useCase.login.LoginUseCase
import com.juank.shopapp.domain.utils.AppDao
import com.juank.shopapp.presentation.screens.login.LoginViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val firebaseModule : Module = module {
    single<FirebaseAuth> { Firebase.auth }
}

val appDao : Module = module {
    single<AppDao> {
        AppDao(productsDao = get())
    }
}

val repositoryModule : Module = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
}

val useCaseModule : Module = module {
    factory { LoginUseCase(get()) }
}

val viewModelModule : Module = module {
    factory { LoginViewModel(get()) }
}

val serviceModule : Module = module {
    single<AuthService> { AuthServiceImpl(get()) }
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