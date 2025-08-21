package com.juank.shopapp.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.juank.shopapp.data.local.database.UserDatabase
import com.juank.shopapp.data.local.database.UserDatabaseFactory
import com.juank.shopapp.data.remote.services.auth.AuthService
import com.juank.shopapp.data.remote.services.auth.AuthServiceImpl
import com.juank.shopapp.data.repositoryImpl.confirmCart.ConfirmCartRepositoryImpl
import com.juank.shopapp.data.repositoryImpl.login.LoginRepositoryImpl
import com.juank.shopapp.data.repositoryImpl.principal.PrincipalRepositoryImpl
import com.juank.shopapp.data.repositoryImpl.shoppingCart.ShoppingCartRepositoryImpl
import com.juank.shopapp.domain.repository.confirmCart.ConfirmCartRepository
import com.juank.shopapp.domain.repository.login.LoginRepository
import com.juank.shopapp.domain.repository.principal.PrincipalRepository
import com.juank.shopapp.domain.repository.shoppingCart.ShoppingCartRepository
import com.juank.shopapp.domain.useCase.confirmCart.ConfirmCartUseCase
import com.juank.shopapp.domain.useCase.login.LoginUseCase
import com.juank.shopapp.domain.useCase.principal.PrincipalUseCase
import com.juank.shopapp.domain.useCase.shoppingCart.ShoppingCartUseCase
import com.juank.shopapp.domain.utils.AppDao
import com.juank.shopapp.presentation.screens.confirmCart.ConfirmCartViewModel
import com.juank.shopapp.presentation.screens.login.LoginViewModel
import com.juank.shopapp.presentation.screens.principal.PrincipalViewModel
import com.juank.shopapp.presentation.screens.shoppingCart.ShoppingCartViewModel
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
        AppDao(
            productsDao = get(),
            categoriesDao = get(),
            cartItemsDao = get())
    }
}

val repositoryModule : Module = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<PrincipalRepository> { PrincipalRepositoryImpl(get(), get()) }
    single<ShoppingCartRepository> { ShoppingCartRepositoryImpl(get()) }
    single<ConfirmCartRepository> { ConfirmCartRepositoryImpl() }
}

val useCaseModule : Module = module {
    factory { LoginUseCase(get()) }
    factory { PrincipalUseCase(get()) }
    factory { ShoppingCartUseCase(get()) }
    factory { ConfirmCartUseCase(get()) }
}

val viewModelModule : Module = module {
    factory { LoginViewModel(get(), get()) }
    factory { PrincipalViewModel(get()) }
    factory { ShoppingCartViewModel(get()) }
    factory { ConfirmCartViewModel(get()) }
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
    single { get<UserDatabase>().categoriesDao() }
    single { get<UserDatabase>().cartItemsDao() }
}