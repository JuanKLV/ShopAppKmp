package com.juank.shopapp.data.di

fun appModule() = listOf(
    dataBaseModule,
    appDao,
    firebaseModule,
    serviceModule,
    repositoryModule,
    viewModelModule,
    useCaseModule,
)