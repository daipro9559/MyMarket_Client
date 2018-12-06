package com.example.dainv.mymarket.di

import com.example.dainv.mymarket.api.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun itemService(retrofit: Retrofit): ItemService {
        return retrofit.create(ItemService::class.java)
    }

    @Singleton
    @Provides
    fun loginService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
    @Singleton
    @Provides
    fun addressService(retrofit: Retrofit): AddressService{
        return retrofit.create(AddressService::class.java)
    }

    @Singleton
    @Provides
    fun standService(retrofit: Retrofit): StandService{
        return retrofit.create(StandService::class.java)
    }
    @Singleton
    @Provides
    fun provideNotificationService(retrofit: Retrofit):NotificationService{
        return retrofit.create(NotificationService::class.java)
    }

    @Singleton
    @Provides
    fun provinceTransactionService(retrofit: Retrofit) = retrofit.create(TransactionService::class.java)
}