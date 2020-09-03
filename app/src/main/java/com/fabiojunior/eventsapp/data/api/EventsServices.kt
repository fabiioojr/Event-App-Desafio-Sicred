package com.fabiojunior.eventsapp.data.api

import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.model.Event
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EventsServices {

    /**
     * Route to retrieve the event list
     */
    @GET("/events")
    fun getEvents(): Call<List<Event>>

    /**
     * Route to check in
     */
    @POST("/checkin")
    fun checkIn(@Body request: CheckIn): Call<CheckIn>
}