package com.fabiojunior.eventsapp.data.api

import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.model.Event
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventsServices {

    /**
     * Route to retrieve the event list
     */
    @GET("/events")
    suspend fun getEvents(): Response<List<Event>>

    /**
     * Route to check in
     */
    @POST("/checkin")
    suspend fun checkIn(@Body request: CheckIn): Response<ResponseBody>
}