package com.juank.shopapp.data.di

fun appModule() = listOf(
    firebaseModule,
    dataBaseModule,
    appDao,
    serviceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
)