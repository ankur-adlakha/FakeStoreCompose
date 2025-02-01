package com.example.fakestoreapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.fakestoreapp.BuildConfig
import com.example.fakestoreapp.network.FSApi
import com.example.fakestoreapp.repositories.IProductsRepository
import com.example.fakestoreapp.repositories.ProductsRepository
import com.example.fakestoreapp.storage.FSCache
import com.example.fakestoreapp.storage.FSDatabase
import com.example.fakestoreapp.storage.daos.ProductDao
import com.example.fakestoreapp.utils.NetworkUtils
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModules {
    companion object {
        private const val CACHE_SIZE = 10L * 1024 * 1024   //10MB
        private const val TIMEOUT = 30L    // Seconds
        private const val DATABASE_NAME = "fake_store_database"
        private const val BASE_URL = "https://fakestoreapi.com/"
    }


    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val builder = it.request().newBuilder()
            builder.addHeader("Content-Type", "application/json")
            val response = it.proceed(builder.build())
            response
        }
    }


    @Singleton
    @Provides
    fun provideAPI(gson: Gson, okHttpClient: OkHttpClient) = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FSApi::class.java)

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        gsonBuilder.excludeFieldsWithoutExposeAnnotation()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(application: Application, interceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.cache(Cache(application.cacheDir, CACHE_SIZE))
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS).connectTimeout(TIMEOUT, TimeUnit.SECONDS)

        builder.interceptors().add(interceptor)

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.networkInterceptors().add(httpLoggingInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    internal fun provideSharedPreferences(context: Context): FSCache =
        FSCache(context)

    @Provides
    @Singleton
    internal fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FSDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    internal fun provideProductsRepository(
        fsApi: FSApi,
        productDao: ProductDao
    ): IProductsRepository =
        ProductsRepository(fsApi, productDao)

    @Provides
    @Singleton
    internal fun provideProductDao(fsDatabase: FSDatabase) = fsDatabase.productDao()

    @Provides
    @Singleton
    internal fun provideNetworkUtils(@ApplicationContext context: Context) = NetworkUtils(context)
}