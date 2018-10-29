package com.example.dainv.mymarket.di

import android.app.Application
import com.example.dainv.mymarket.base.Constant
import com.example.dainv.mymarket.util.LiveDataCallAdapterFactory
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, ServiceModule::class, ServiceAppModule::class])
class AppModule {
    @Provides
    @Singleton
    fun provideContext(app: Application) = app.applicationContext

    //    @Provides
//    @Singleton
//    fun appDatabase(context: Context): AppDatabase {
//        return Room.databaseBuilder(context, AppDatabase::class.java, Constant.APP_NAME).build()
//    }
    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.BASE_URL)
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun provideGson() = Gson()

}