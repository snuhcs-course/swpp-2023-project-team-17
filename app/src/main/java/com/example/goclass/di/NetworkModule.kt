package com.example.goclass.di

import com.example.goclass.R
import com.example.goclass.network.ServiceApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule =
    module {
        single {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            loggingInterceptor
        }

        // OkHttpClient 설정 (로깅 인터셉터 포함)
        single {
            OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .build()
        }

        single<Retrofit> {
            val baseUrl = androidContext().getString(R.string.base_url)
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get<OkHttpClient>())
                .build()
        }

        single<ServiceApi> { get<Retrofit>().create(ServiceApi::class.java) }
    }
