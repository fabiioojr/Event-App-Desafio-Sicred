package com.fabiojunior.eventsapp.data.repository

import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.model.Event

interface EventDataSourceInterface {

    suspend fun getEvents(): List<Event>?

    suspend fun checkIn(request: CheckIn): Boolean
}