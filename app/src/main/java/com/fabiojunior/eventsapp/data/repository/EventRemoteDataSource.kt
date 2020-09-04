package com.fabiojunior.eventsapp.data.repository

import com.fabiojunior.eventsapp.data.api.EventsServices
import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.model.Event

class EventRemoteDataSource(private val apiService: EventsServices) : EventDataSourceInterface {
    override suspend fun getEvents(): List<Event>? {
        return apiService.getEvents().body()
    }

    override suspend fun checkIn(request: CheckIn): Boolean {
        return apiService.checkIn(request).isSuccessful
    }
}