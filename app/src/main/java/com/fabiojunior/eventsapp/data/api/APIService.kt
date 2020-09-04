package com.fabiojunior.eventsapp.data.api

import com.fabiojunior.eventsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIService {

    companion object {
        private const val TIMEOUT: Long = 60
    }

    private fun logInterceptor(): HttpLoggingInterceptor {
        val logInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return logInterceptor
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor())
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS).build()
    }

    private fun createNetworkClient(): Retrofit = Retrofit.Builder()
        .baseUrl("http://5b840ba5db24a100142dcd8c.mockapi.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    fun eventApiService(): EventsServices =
        createNetworkClient().create(EventsServices::class.java)

}