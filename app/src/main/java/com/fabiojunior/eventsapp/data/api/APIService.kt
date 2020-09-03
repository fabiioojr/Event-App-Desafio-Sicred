package com.fabiojunior.eventsapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {
    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://5b840ba5db24a100142dcd8c.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun service() = initRetrofit().create(EventsServices::class.java)
}