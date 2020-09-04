package com.fabiojunior.eventsapp.data.repository

import com.fabiojunior.eventsapp.data.model.CheckIn
import com.fabiojunior.eventsapp.data.model.Event

class DataRepository(private val remoteDataSource: EventRemoteDataSource) :
    DataRepositoryInterface {

    /**
     * Repository for get events and treat error
     */
    override suspend fun getEvents(): List<Event>? {
        return try {
            remoteDataSource.getEvents()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Repository for checkin and treat error
     */
    override suspend fun checkIn(request: CheckIn): Boolean {
        return try {
            remoteDataSource.checkIn(request)
        } catch (e: Exception) {
            false
        }
    }
}