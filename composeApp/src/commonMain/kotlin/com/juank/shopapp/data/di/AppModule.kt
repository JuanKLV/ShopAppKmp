package com.juank.shopapp.data.di

fun appModule() = listOf(
    dataBaseModule,
    appDao,
    viewModelModule,
    useCaseModule,
    repositoryModule
)